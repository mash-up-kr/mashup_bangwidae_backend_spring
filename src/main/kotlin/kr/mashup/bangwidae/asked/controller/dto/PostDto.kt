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
    @ApiModelProperty(value = "생성일", example = "2022-07-14T23:57:33.436")
    val createdAt: LocalDateTime?,
    @ApiModelProperty(value = "수정일", example = "2022-07-14T23:57:33.436")
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
    @ApiModelProperty(value = "글 작성자 id", example = "62d81cd92336cf4bb5b6c6ab")
    val id: String,
    @ApiModelProperty(value = "글 작성자 tag list", example = "mbti, entj")
    val tags: List<String> = emptyList(),
    @ApiModelProperty(value = "글 작성자 닉네임", example = "sample nickname")
    val nickname: String
)