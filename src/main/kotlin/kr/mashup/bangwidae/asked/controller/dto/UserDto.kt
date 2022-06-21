package kr.mashup.bangwidae.asked.controller.dto

import io.swagger.annotations.ApiModelProperty
import kr.mashup.bangwidae.asked.model.User

data class UserDto(
    @ApiModelProperty(value = "user id", example = "gardenlee")
    val id: String,
    @ApiModelProperty(value = "user nickname", example = "gardenlee")
    val nickname: String
) {
    companion object {
        fun from(user: User): UserDto {
            return UserDto(
                id = user.id!!.toHexString(),
                nickname = user.nickname
            )
        }
    }
}

data class JoinUserRequest(
    @ApiModelProperty(value = "로그인 ID", example = "gardenlee")
    val loginId: String,
    @ApiModelProperty(value = "로그인 비밀번호", example = "12345")
    val password: String
)

data class JoinUserResponse(
    @ApiModelProperty(value = "인증 토큰", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.~~~")
    val accessToken: String
)