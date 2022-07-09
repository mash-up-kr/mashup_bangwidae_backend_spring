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
    @ApiModelProperty(value = "post id", example = "62b49a12507aeb02e6534572")
    val id: String,
    @ApiModelProperty(value = "작성자 user id", example = "62b49a12507aeb02e6534572")
    val user: PostWriter,
    @ApiModelProperty(value = "post content", example = "질문 post 샘플")
    val content: String = "",
    @ApiModelProperty(value = "경도", example = "127.4")
    val longitude: Double,
    @ApiModelProperty(value = "위도", example = "23.5")
    val latitude: Double,
    @ApiModelProperty(value = "대표 주소", example = "경기도 성남시 분당구 불정로 6")
    val representativeAddress: String?,
    @ApiModelProperty(value = "생성일", example = "2022-06-23T16:51:30.717+00:00")
    val createdAt: LocalDateTime?,
    @ApiModelProperty(value = "수정일", example = "2022-06-23T16:51:30.717+00:00")
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
    @ApiModelProperty(value = "post content", example = "질문 post 샘플")
    val content: String,
    @ApiModelProperty(value = "경도", example = "127.4")
    val longitude: Double,
    @ApiModelProperty(value = "위도", example = "23.5")
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
    @ApiModelProperty(value = "post content", example = "질문 post 샘플")
    val content: String?,
    @ApiModelProperty(value = "경도(nullable)", example = "127.4")
    val longitude: Double?,
    @ApiModelProperty(value = "위도(nullable)", example = "23.5")
    val latitude: Double?
)

data class PostWriter(
    @ApiModelProperty(value = "작성자 user id", example = "62b49a12507aeb02e6534572")
    val id: String,
    @ApiModelProperty(value = "작성자 태그 리스트", example = "['MBTI', '모임', '방탈출']")
    val tags: List<String> = emptyList(),
    @ApiModelProperty(value = "작성자 닉네임", example = "니모를 찾아서")
    val nickname: String
)