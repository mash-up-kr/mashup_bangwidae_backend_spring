package kr.mashup.bangwidae.asked.controller.dto

import kr.mashup.bangwidae.asked.model.User

data class UserInfoDto(
    val userId: String,
    val nickname: String?,
    val profileDescription: String?,
    val tags: List<String>,
) {
    companion object {
        fun from(user: User): UserInfoDto {
            return UserInfoDto(
                userId = user.id!!.toHexString(),
                nickname = user.nickname,
                profileDescription = user.description,
                tags = user.tags,
            )
        }
    }
}

data class JoinUserRequest(
    val email: String,
    val password: String
)

data class JoinUserResponse(
    val accessToken: String,
    val refreshToken: String
)

data class UpdateNicknameRequest(
    val nickname: String
)

data class UpdateProfileRequest(
    val description: String,
    val tags: List<String>,
)
