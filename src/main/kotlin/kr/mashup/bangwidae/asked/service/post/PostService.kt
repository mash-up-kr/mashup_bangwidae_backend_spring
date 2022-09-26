package kr.mashup.bangwidae.asked.service.post

import kr.mashup.bangwidae.asked.controller.dto.PostEditRequest
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.model.document.post.Post
import kr.mashup.bangwidae.asked.model.domain.PostDomain
import kr.mashup.bangwidae.asked.repository.PostRepository
import kr.mashup.bangwidae.asked.repository.UserRepository
import kr.mashup.bangwidae.asked.service.BlackListComponent
import kr.mashup.bangwidae.asked.service.ReportService
import kr.mashup.bangwidae.asked.service.levelpolicy.LevelPolicyService
import kr.mashup.bangwidae.asked.service.place.PlaceService
import kr.mashup.bangwidae.asked.utils.GeoUtils
import kr.mashup.bangwidae.asked.utils.getLatitude
import kr.mashup.bangwidae.asked.utils.getLongitude
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository: PostRepository,
    private val placeService: PlaceService,
    private val userRepository: UserRepository,
    private val postLikeService: PostLikeService,
    private val commentService: CommentService,
    private val levelPolicyService: LevelPolicyService,
    private val blackListComponent: BlackListComponent,
    private val reportService: ReportService
) : WithPostAuthorityValidator {
    fun findById(id: ObjectId): Post {
        return postRepository.findByIdAndDeletedFalse(id)
            ?: throw DoriDoriException.of(DoriDoriExceptionType.NOT_EXIST)
    }

    fun write(user: User, post: Post): PostDomain {
        return postRepository.save(updatePlaceInfo(post)).also {
            levelPolicyService.levelUpIfConditionSatisfied(user)
        }.toDomain(user)
    }

    fun edit(user: User, postId: ObjectId, request: PostEditRequest): PostDomain {
        val post = findById(postId)
            .also { it.validateToUpdate(user) }
        return postRepository.save(updatePlaceInfo(post.update(request))).toDomain(user)
    }

    fun delete(user: User, postId: ObjectId) {
        val post = findById(postId).also { it.validateToDelete(user) }
        postRepository.save(post.delete())
    }

    fun getNearPost(
        user: User?, longitude: Double, latitude: Double, meterDistance: Double, lastId: ObjectId?, size: Int
    ): List<PostDomain> {
        val location = GeoUtils.geoJsonPoint(longitude, latitude)
        val distance = Distance(meterDistance / 1000, Metrics.KILOMETERS)
        val postList = postRepository.findByLocationNearAndIdBeforeAndDeletedFalseOrderByIdDesc(
            location,
            lastId ?: ObjectId(),
            distance,
            PageRequest.of(0, size)
        )

        return postList.toDomain(user)
    }

    fun getPostById(user: User?, id: ObjectId): PostDomain {
        val post = findById(id)
        return post.toDomain(user)
    }

    private fun updatePlaceInfo(post: Post): Post {
        val longitude = post.location.getLongitude()
        val latitude = post.location.getLatitude()
        val region = placeService.reverseGeocode(longitude, latitude)
        return post.copy(representativeAddress = region.representativeAddress, region = region)
    }

    fun findByFromUser(user: User, lastId: ObjectId?, size: Int): List<PostDomain> {
        val postList = postRepository.findByUserIdAndIdBeforeAndDeletedFalseOrderByIdDesc(
            userId = user.id!!,
            lastId = lastId ?: ObjectId(),
            pageRequest = PageRequest.of(0, size)
        )

        return postList.toDomain(user)
    }

    private fun Post.toDomain(user: User?): PostDomain {
        return listOf(this).toDomain(user).first()
    }

    private fun List<Post>.toDomain(user: User?): List<PostDomain> {
        val userMap = userRepository.findAllByIdIn(this.map { it.userId }.toSet()).associateBy { it.id!! }
        val likeMap = postLikeService.getLikeMap(this)
        val commentCountMap = commentService.getCommentCountMap(this)
        val blackListMap = blackListComponent.getBlackListMap().get()
        val reportMap = reportService.getReportMap().get()
        return this.map { post ->
            val blocked = blackListMap[user!!.id]?.contains(post.userId) == true
            val reported = reportMap[user.id]?.map { it.targetId }?.contains(post.id!!) == true
            if (blocked || reported) {
                PostDomain.from(
                    user = userMap[post.userId]!!.getAnonymousUser(),
                    post = if (reported) post.toReportedPost() else post.toBlockedPost(),
                    likeCount = 0,
                    commentCount = 0,
                    userLiked = false
                )
            } else {
                PostDomain.from(
                    user = userMap[post.userId]!!,
                    post = post,
                    likeCount = likeMap[post.id]?.size ?: 0,
                    commentCount = commentCountMap[post.id] ?: 0,
                    userLiked = likeMap[post.id]?.map { like -> like.userId }?.contains(user.id) ?: false
                )
            }
        }
    }
}