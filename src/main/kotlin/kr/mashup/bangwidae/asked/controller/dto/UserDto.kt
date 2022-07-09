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
                nickname = user.nickname!!
            )
        }
    }
}

data class JoinUserRequest(
    @ApiModelProperty(value = "이메일", example = "doridori@gmail.com")
    val email: String,
    @ApiModelProperty(value = "로그인 비밀번호", example = "12345")
    val password: String
)

data class JoinUserResponse(
    @ApiModelProperty(value = "인증 토큰", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.~~~")
    val accessToken: String
)

data class UpdateNicknameRequest (
    @ApiModelProperty(value = "닉네임", example = "도리를찾아서")
    val nickname: String
)

data class UpdateProfileRequest (
    @ApiModelProperty(value = "프로필 소개", example = "안녕하세요! 도리도리입니다.")
    val description: String,
    @ApiModelProperty(value = "관심사 리스트", example = "[MBTI, 넷플릭스]")
    val tags: List<String>,
)

