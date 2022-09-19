package kr.mashup.bangwidae.asked.model.domain

import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.model.document.post.Post
import kr.mashup.bangwidae.asked.utils.getLatitude
import kr.mashup.bangwidae.asked.utils.getLongitude
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class PostDomain(
    val id: ObjectId,
    val user: WriterUserDomain,
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
            anonymous = post.anonymous,
            createdAt = post.createdAt,
            updatedAt = post.updatedAt,
            user = WriterUserDomain.of(user, post.anonymous),
            likeCount = likeCount,
            commentCount = commentCount,
            userLiked = userLiked,
        )
    }
}
