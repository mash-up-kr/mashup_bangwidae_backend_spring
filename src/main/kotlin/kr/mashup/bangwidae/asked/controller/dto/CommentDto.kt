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
    val latitude: Double
) {
    fun toEntity(userId: ObjectId, postId: ObjectId): Comment {
        return Comment(
            content = content,
            userId = userId,
            postId = postId,
            location = GeoUtils.geoJsonPoint(longitude, latitude)
        )
    }
}

data class CommentEditRequest(
    @ApiModelProperty(value = "댓글 내용", example = "댓글 내용 string")
    val content: String,
    val longitude: Double?,
    val latitude: Double?,
)

data class CommentDto(
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
        fun from(user: User, comment: Comment): CommentDto {
            return CommentDto(
                id = comment.id!!.toHexString(),
                user = CommentWriter(
                    id = user.id!!.toHexString(),
                    tags = user.tags,
                    nickname = user.nickname!!
                ),
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
    val nickname: String
)