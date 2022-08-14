package kr.mashup.bangwidae.asked.service.post

import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.post.Post
import kr.mashup.bangwidae.asked.utils.getLatitude
import kr.mashup.bangwidae.asked.utils.getLongitude
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class PostDomain(
    val id: ObjectId,
    val user: PostUserDomain,
    val content: String,
    val likeCount: Int,
    val commentCount: Int,
    val userLiked: Boolean,
    val longitude: Double,
    val latitude: Double,
    val representativeAddress: String?,
    val anonymous: Boolean,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun from(user: User, post: Post, likeCount: Int, commentCount: Int, userLiked: Boolean) = PostDomain(
            id = post.id!!,
            content = post.content,
            representativeAddress = post.representativeAddress,
            longitude = post.location.getLongitude(),
            latitude = post.location.getLatitude(),
            anonymous = post.anonymous ?: false,
            createdAt = post.createdAt,
            updatedAt = post.updatedAt,
            user = if (post.anonymous == true) PostUserDomain.from(user.getAnonymousUser())
            else PostUserDomain.from(user),
            likeCount = likeCount,
            commentCount = commentCount,
            userLiked = userLiked,
        )
    }
}

data class PostUserDomain(
    val id: ObjectId,
    val tags: List<String>,
    val nickname: String,
    val profileImageUrl: String,
    val level: Int
) {
    companion object {
        fun from(user: User) = PostUserDomain(
            id = user.id!!,
            nickname = user.nickname!!,
            tags = user.tags,
            profileImageUrl = user.userProfileImageUrl,
            level = user.level
        )
    }
}