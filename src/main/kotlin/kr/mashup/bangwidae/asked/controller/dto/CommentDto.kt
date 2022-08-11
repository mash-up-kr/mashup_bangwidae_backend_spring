package kr.mashup.bangwidae.asked.controller.dto

import io.swagger.annotations.ApiModelProperty
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.post.Comment
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
    val anonymous: Boolean?
) {
    fun toEntity(userId: ObjectId, postId: ObjectId): Comment {
        return Comment(
            content = content,
            userId = userId,
            postId = postId,
            location = GeoUtils.geoJsonPoint(longitude, latitude),
            anonymous = anonymous ?: false
        )
    }
}

data class CommentEditRequest(
    @ApiModelProperty(value = "댓글 내용", example = "댓글 내용 string")
    val content: String,
    val longitude: Double?,
    val latitude: Double?,
    val anonymous: Boolean?
)

data class CommentDto(
    @ApiModelProperty(value = "댓글 id", example = "62c6f1ede8802854c463b5f5")
    val id: String,
    @ApiModelProperty(value = "댓글 작성자 User")
    val user: CommentWriter,
    @ApiModelProperty(value = "댓글 내용", example = "댓글 내용 string")
    val content: String,
    @ApiModelProperty(value = "좋아요 개수", example = "10")
    val likeCount: Int,
    @ApiModelProperty(value = "사용자가 좋아요 누른 여부", example = "true")
    val userLiked: Boolean,
    @ApiModelProperty(value = "대표 주소", example = "분당구")
    val representativeAddress: String?,
    @ApiModelProperty(value = "익명 여부", example = "true")
    val anonymous: Boolean,
    @ApiModelProperty(value = "생성일", example = "2022-07-14T23:57:33.436")
    val createdAt: LocalDateTime?,
    @ApiModelProperty(value = "수정일", example = "2022-07-14T23:57:33.436")
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun from(user: User, comment: Comment, likeCount: Int, userLiked: Boolean): CommentDto {
            return CommentDto(
                id = comment.id!!.toHexString(),
                user = comment.getWriter(user),
                content = comment.content,
                likeCount = likeCount,
                userLiked = userLiked,
                representativeAddress = comment.region?.representativeAddress,
                anonymous = comment.anonymous ?: false,
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt
            )
        }
    }
}

data class CommentResultDto(
    @ApiModelProperty(value = "댓글 id", example = "62c6f1ede8802854c463b5f5")
    val id: String,
    @ApiModelProperty(value = "댓글 작성자 User")
    val user: CommentWriter,
    @ApiModelProperty(value = "댓글 내용", example = "댓글 내용 string")
    val content: String,
    @ApiModelProperty(value = "대표 주소", example = "분당구")
    val representativeAddress: String?,
    @ApiModelProperty(value = "생성일", example = "2022-07-14T23:57:33.436")
    val createdAt: LocalDateTime?,
    @ApiModelProperty(value = "수정일", example = "2022-07-14T23:57:33.436")
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun from(user: User, comment: Comment): CommentResultDto {
            return CommentResultDto(
                id = comment.id!!.toHexString(),
                user = comment.getWriter(user),
                content = comment.content,
                representativeAddress = comment.region?.representativeAddress,
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt
            )
        }
    }
}

data class CommentWriter(
    @ApiModelProperty(value = "User id", example = "62c6f1ede8802854c463b5f5")
    val id: String,
    @ApiModelProperty(value = "User 태그리스트", example = "[MBTI, 산책]")
    val tags: List<String> = emptyList(),
    @ApiModelProperty(value = "User 닉네임", example = "noname")
    val nickname: String,
    @ApiModelProperty(value = "글 작성자 프로필 이미지", example = "http://image.com")
    val profileImageUrl: String?,
    @ApiModelProperty(value = "User 레벨", example = "13")
    val level: Int
) {
    companion object {
        fun from(user: User): CommentWriter {
            return CommentWriter(
                id = user.id!!.toHexString(),
                tags = user.tags,
                nickname = user.nickname!!,
                profileImageUrl = user.profileImageUrl,
                level = user.level
            )
        }
    }
}