package kr.mashup.bangwidae.asked.service.post

import kr.mashup.bangwidae.asked.controller.dto.CommentEditRequest
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.model.document.post.Comment
import kr.mashup.bangwidae.asked.model.document.post.Post
import kr.mashup.bangwidae.asked.model.domain.CommentDomain
import kr.mashup.bangwidae.asked.repository.CommentRepository
import kr.mashup.bangwidae.asked.repository.UserRepository
import kr.mashup.bangwidae.asked.service.BlackListComponent
import kr.mashup.bangwidae.asked.service.ReportService
import kr.mashup.bangwidae.asked.service.event.CommentWriteEvent
import kr.mashup.bangwidae.asked.service.levelpolicy.LevelPolicyService
import kr.mashup.bangwidae.asked.service.place.PlaceService
import kr.mashup.bangwidae.asked.utils.getLatitude
import kr.mashup.bangwidae.asked.utils.getLongitude
import org.bson.types.ObjectId
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service


@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val placeService: PlaceService,
    private val commentLikeService: CommentLikeService,
    private val levelPolicyService: LevelPolicyService,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val blackListComponent: BlackListComponent,
    private val reportService: ReportService
) : WithPostAuthorityValidator, WithCommentAuthorityValidator {
    fun findById(commentId: ObjectId): Comment {
        return commentRepository.findByIdAndDeletedFalse(commentId)
            ?: throw DoriDoriException.of(DoriDoriExceptionType.NOT_EXIST)
    }

    fun getCommentsByPostId(user: User?, postId: ObjectId, lastId: ObjectId?, size: Int): List<CommentDomain> {
        val commentList = commentRepository.findByPostIdAndIdBeforeAndDeletedFalseOrderByIdDesc(
            postId,
            lastId ?: ObjectId(),
            PageRequest.of(0, size)
        )
        return commentList.toDomain(user)
    }

    fun getCommentCountMap(postList: List<Post>): Map<ObjectId, Int> {
        val commentList = commentRepository.findAllByPostIdIn(postList.mapNotNull { it.id })
        return commentList.groupingBy { it.postId }.eachCount()
    }

    fun write(user: User, comment: Comment): CommentDomain {
        return commentRepository.save(updatePlaceInfo(comment)).toDomain(user)
            .also {
                levelPolicyService.levelUpIfConditionSatisfied(user)
            }.also {
                applicationEventPublisher.publishEvent(CommentWriteEvent(it.id))
            }

    }

    fun edit(user: User, commentId: ObjectId, request: CommentEditRequest): CommentDomain {
        val comment = findById(commentId).also { it.validateToUpdate(user) }
        return commentRepository.save(updatePlaceInfo(comment.update(request))).toDomain(user)
    }

    fun delete(user: User, commentId: ObjectId) {
        val comment = findById(commentId).also { it.validateToDelete(user) }
        commentRepository.save(comment.delete())
    }

    private fun updatePlaceInfo(comment: Comment): Comment {
        val longitude = comment.location.getLongitude()
        val latitude = comment.location.getLatitude()
        val region = placeService.reverseGeocode(longitude, latitude)
        return comment.copy(region = region)
    }

    private fun Comment.toDomain(user: User?): CommentDomain {
        return listOf(this).toDomain(user).first()
    }

    private fun List<Comment>.toDomain(user: User?): List<CommentDomain> {
        val userMap = userRepository.findAllByIdIn(this.map { it.userId }.toSet()).associateBy { it.id!! }
        val likeMap = commentLikeService.getLikeMap(this)
        val blackListMap = blackListComponent.getBlackListMap().get()
        val reportMap = reportService.getReportMap().get()
        return this.map { comment ->
            val blocked = blackListMap[user!!.id]?.contains(comment.userId) == true
            val reported = reportMap[user.id]?.map { it.targetId }?.contains(comment.id!!) == true
            if (blocked || reported) {
                CommentDomain.from(
                    user = userMap[comment.userId]!!,
                    comment = if (reported) comment.toReportedComment() else comment.toBlockedComment(),
                    likeCount = 0,
                    userLiked = false
                )
            } else {
                CommentDomain.from(
                    user = userMap[comment.userId]!!.getAnonymousUser(),
                    comment = comment,
                    likeCount = likeMap[comment.id]?.size ?: 0,
                    userLiked = likeMap[comment.id]?.map { like -> like.userId }?.contains(user.id) ?: false
                )
            }
        }
    }
}