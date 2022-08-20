package kr.mashup.bangwidae.asked.controller.dto

import kr.mashup.bangwidae.asked.model.document.post.Post
import kr.mashup.bangwidae.asked.model.domain.PostDomain
import kr.mashup.bangwidae.asked.model.domain.PostUserDomain
import kr.mashup.bangwidae.asked.utils.GeoUtils
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class PostDto(
    val id: String,
    val user: PostWriter,
    val content: String = "",
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
        fun from(post: PostDomain) =
            PostDto(
                id = post.id.toHexString(),
                user = PostWriter.from(post.user),
                content = post.content,
                likeCount = post.likeCount,
                userLiked = post.userLiked,
                commentCount = post.commentCount,
                longitude = post.longitude,
                latitude = post.latitude,
                representativeAddress = post.representativeAddress,
                anonymous = post.anonymous,
                createdAt = post.createdAt,
                updatedAt = post.updatedAt,
            )
    }
}

data class PostWriteRequest(
    val content: String,
    val longitude: Double,
    val latitude: Double,
    val anonymous: Boolean
) {
    fun toEntity(userId: ObjectId): Post {
        return Post(
            content = content,
            userId = userId,
            location = GeoUtils.geoJsonPoint(longitude, latitude),
            anonymous = anonymous
        )
    }
}

data class PostEditRequest(
    val content: String?,
    val longitude: Double?,
    val latitude: Double?,
    val anonymous: Boolean?
)

data class PostWriter(
    val id: String,
    val tags: List<String> = emptyList(),
    val nickname: String,
    val profileImageUrl: String?,
    val level: Int
) {
    companion object {
        fun from(user: PostUserDomain): PostWriter {
            return PostWriter(
                id = user.id.toHexString(),
                tags = user.tags,
                nickname = user.nickname,
                profileImageUrl = user.profileImageUrl,
                level = user.level
            )
        }
    }
}