package kr.mashup.bangwidae.asked.service.post

import kr.mashup.bangwidae.asked.controller.dto.CommentDto
import kr.mashup.bangwidae.asked.controller.dto.CommentEditRequest
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.post.Comment
import kr.mashup.bangwidae.asked.model.post.Post
import kr.mashup.bangwidae.asked.repository.CommentRepository
import kr.mashup.bangwidae.asked.repository.UserRepository
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
) : WithPostAuthorityValidator, WithCommentAuthorityValidator {
    fun findById(commentId: ObjectId): Comment {
        return commentRepository.findByIdAndDeletedFalse(commentId)
            ?: throw DoriDoriException.of(DoriDoriExceptionType.NOT_EXIST)
    }

    fun getCommentsByPostId(
        user: User?, postId: ObjectId, lastId: ObjectId?, size: Int
    ): List<CommentDto> {
        val commentList = commentRepository.findByPostIdAndIdBeforeAndDeletedFalseOrderByIdDesc(
            postId,
            lastId ?: ObjectId(),
            PageRequest.of(0, size)
        )
        val userIdList = commentList.map { it.userId }.distinct()
        val userMap = userRepository.findAllByIdIn(userIdList).associateBy { it.id }
        val likeMap = commentLikeService.getLikeMap(commentList)
        return commentList.map { comment ->
            CommentDto.from(
                user = userMap[comment.userId]!!,
                comment = comment,
                likeCount = likeMap[comment.id]?.size ?: 0,
                userLiked = if (user == null) false
                else likeMap[comment.id]?.map { like -> like.userId }?.contains(user.id) ?: false
            )
        }
    }

    fun getCommentCountMap(postList: List<Post>): Map<ObjectId, Int> {
        val commentList = commentRepository.findAllByPostIdIn(postList.mapNotNull { it.id })
        return commentList.groupingBy { it.postId }.eachCount()
    }

    fun write(user: User, comment: Comment): Comment {
        return commentRepository.save(updatePlaceInfo(comment)).also {
            levelPolicyService.levelUpIfConditionSatisfied(user)
        }.also {
            applicationEventPublisher.publishEvent(CommentWriteEvent(it.id!!))
        }
    }

    fun edit(user: User, commentId: ObjectId, request: CommentEditRequest): Comment {
        val comment = findById(commentId).also { it.validateToUpdate(user) }
        return commentRepository.save(updatePlaceInfo(comment.update(request)))
    }

    fun delete(user: User, commentId: ObjectId): Comment {
        val comment = findById(commentId).also { it.validateToDelete(user) }
        return commentRepository.save(comment.delete())
    }

    private fun updatePlaceInfo(comment: Comment): Comment {
        val longitude = comment.location.getLongitude()
        val latitude = comment.location.getLatitude()
        val region = placeService.reverseGeocode(longitude, latitude)
        return comment.copy(region = region)
    }
}