package kr.mashup.bangwidae.asked.controller.dto

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
