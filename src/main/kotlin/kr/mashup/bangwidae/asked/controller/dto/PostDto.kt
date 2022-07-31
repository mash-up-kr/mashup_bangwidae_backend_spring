package kr.mashup.bangwidae.asked.controller.dto

import io.swagger.annotations.ApiModelProperty
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.post.Post
import kr.mashup.bangwidae.asked.utils.GeoUtils
import kr.mashup.bangwidae.asked.utils.getLatitude
import kr.mashup.bangwidae.asked.utils.getLongitude
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class PostResultDto(
    @ApiModelProperty(value = "포스트 글 id", example = "62d81cd92336cf4bb5b6c6ab")
    val id: String,
    @ApiModelProperty(value = "포스트 글 작성자", example = "")
    val user: PostWriter,
    @ApiModelProperty(value = "포스트 글 내용", example = "글")
    val content: String = "",
    @ApiModelProperty(value = "경도", example = "136.4")
    val longitude: Double,
    @ApiModelProperty(value = "위도", example = "36.4")
    val latitude: Double,
    @ApiModelProperty(value = "대표주소", example = "강남구")
    val representativeAddress: String?,
    @ApiModelProperty(value = "익명 여부", example = "true")
    val anonymous: Boolean,
    @ApiModelProperty(value = "생성일", example = "2022-07-14T23:57:33.436")
    val createdAt: LocalDateTime?,
    @ApiModelProperty(value = "수정일", example = "2022-07-14T23:57:33.436")
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun from(user: User, post: Post): PostResultDto {
            return PostResultDto(
                id = post.id!!.toHexString(),
                user = PostWriter.from(post.getWriter(user)),
                content = post.content,
                longitude = post.location.getLongitude(),
                latitude = post.location.getLatitude(),
                representativeAddress = post.representativeAddress,
                anonymous = post.anonymous ?: false,
                createdAt = post.createdAt,
                updatedAt = post.updatedAt,
            )
        }
    }
}

data class PostDto(
    @ApiModelProperty(value = "포스트 글 id", example = "62d81cd92336cf4bb5b6c6ab")
    val id: String,
    @ApiModelProperty(value = "포스트 글 작성자", example = "")
    val user: PostWriter,
    @ApiModelProperty(value = "포스트 글 내용", example = "글")
    val content: String = "",
    @ApiModelProperty(value = "좋아요 개수", example = "10")
    val likeCount: Int,
    @ApiModelProperty(value = "댓글 개수", example = "10")
    val commentCount: Int,
    @ApiModelProperty(value = "사용자가 좋아요 누른 여부", example = "true")
    val userLiked: Boolean,
    @ApiModelProperty(value = "경도", example = "136.4")
    val longitude: Double,
    @ApiModelProperty(value = "위도", example = "36.4")
    val latitude: Double,
    @ApiModelProperty(value = "대표주소", example = "강남구")
    val representativeAddress: String?,
    @ApiModelProperty(value = "익명 여부", example = "true")
    val anonymous: Boolean,
    @ApiModelProperty(value = "생성일", example = "2022-07-14T23:57:33.436")
    val createdAt: LocalDateTime?,
    @ApiModelProperty(value = "수정일", example = "2022-07-14T23:57:33.436")
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun from(
            user: User,
            post: Post,
            likeCount: Int,
            commentCount: Int,
            userLiked: Boolean
        ): PostDto {
            return PostDto(
                id = post.id!!.toHexString(),
                user = PostWriter.from(post.getWriter(user)),
                content = post.content,
                likeCount = likeCount,
                userLiked = userLiked,
                commentCount = commentCount,
                longitude = post.location.getLongitude(),
                latitude = post.location.getLatitude(),
                representativeAddress = post.representativeAddress,
                anonymous = post.anonymous ?: false,
                createdAt = post.createdAt,
                updatedAt = post.updatedAt,
            )
        }
    }
}

data class PostWriteRequest(
    val content: String,
    val longitude: Double,
    val latitude: Double,
    val anonymous: Boolean?
) {
    fun toEntity(userId: ObjectId): Post {
        return Post(
            content = content,
            userId = userId,
            location = GeoUtils.geoJsonPoint(longitude, latitude),
            anonymous = anonymous ?: false
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
    @ApiModelProperty(value = "글 작성자 id", example = "62d81cd92336cf4bb5b6c6ab")
    val id: String,
    @ApiModelProperty(value = "글 작성자 tag list", example = "mbti, entj")
    val tags: List<String> = emptyList(),
    @ApiModelProperty(value = "글 작성자 닉네임", example = "sample nickname")
    val nickname: String,
    @ApiModelProperty(value = "글 작성자 프로필 이미지", example = "http://image.com")
    val profileImageUrl: String?
) {
    companion object {
        fun from(user: User): PostWriter {
            return PostWriter(
                id = user.id!!.toHexString(),
                tags = user.tags,
                nickname = user.nickname!!,
                profileImageUrl = user.profileImageUrl
            )
        }
    }
}