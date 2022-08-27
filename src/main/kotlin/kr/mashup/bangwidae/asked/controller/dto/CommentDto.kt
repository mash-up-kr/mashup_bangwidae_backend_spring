package kr.mashup.bangwidae.asked.controller.dto

import io.swagger.annotations.ApiModelProperty
import kr.mashup.bangwidae.asked.model.document.post.Comment
import kr.mashup.bangwidae.asked.model.domain.CommentDomain
import kr.mashup.bangwidae.asked.utils.GeoUtils
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class CommentWriteRequest(
    @ApiModelProperty(value = "댓글 내용", example = "댓글 내용 string")
    val content: String,
    @ApiModelProperty(value = "경도", example = "136.4")
    val longitude: Double,
    @ApiModelProperty(value = "위도", example = "36.4")
    val latitude: Double,
    @ApiModelProperty(value = "익명 여부", example = "true || null || false")
    val anonymous: Boolean
) {
    fun toEntity(userId: ObjectId, postId: ObjectId): Comment {
        return Comment(
            content = content,
            userId = userId,
            postId = postId,
            location = GeoUtils.geoJsonPoint(longitude, latitude),
            anonymous = anonymous
        )
    }
}

data class CommentEditRequest(
    @ApiModelProperty(value = "댓글 내용", example = "댓글 내용 string")
    val content: String?,
    val longitude: Double?,
    val latitude: Double?,
    val anonymous: Boolean?
)

data class CommentDto(
    val id: String,
    val user: WriterUserDto,
    val content: String,
    val likeCount: Int,
    val userLiked: Boolean,
    val representativeAddress: String,
    val anonymous: Boolean,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun from(comment: CommentDomain): CommentDto {
            return CommentDto(
                id = comment.id.toHexString(),
                user = WriterUserDto.from(comment.user),
                content = comment.content,
                likeCount = comment.likeCount,
                userLiked = comment.userLiked,
                representativeAddress = comment.representativeAddress,
                anonymous = comment.anonymous,
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt
            )
        }
    }
}
