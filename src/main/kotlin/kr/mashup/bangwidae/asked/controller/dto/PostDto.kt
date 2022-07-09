package kr.mashup.bangwidae.asked.controller.dto

import io.swagger.annotations.ApiModelProperty
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.post.Post
import kr.mashup.bangwidae.asked.utils.GeoUtils
import kr.mashup.bangwidae.asked.utils.getLatitude
import kr.mashup.bangwidae.asked.utils.getLongitude
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class PostDto(
    val id: String,
    val user: PostWriter,
    val content: String = "",
    val longitude: Double,
    val latitude: Double,
    val representativeAddress: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun from(user: User, post: Post): PostDto {
            return PostDto(
                id = post.id!!.toHexString(),
                user = PostWriter(
                    id = user.id!!.toHexString(),
                    tags = user.tags,
                    nickname = user.nickname!!
                ),
                content = post.content,
                longitude = post.location.getLongitude(),
                latitude = post.location.getLatitude(),
                representativeAddress = post.representativeAddress,
                createdAt = post.createdAt,
                updatedAt = post.updatedAt,
            )
        }
    }
}

data class PostWriteRequest(
    val content: String,
    val longitude: Double,
    val latitude: Double
) {
    fun toEntity(userId: ObjectId): Post {
        return Post(
            content = content,
            userId = userId,
            location = GeoUtils.geoJsonPoint(longitude, latitude)
        )
    }
}

data class PostEditRequest(
    val content: String?,
    val longitude: Double?,
    val latitude: Double?
)

data class PostWriter(
    val id: String,
    val tags: List<String> = emptyList(),
    val nickname: String
)