package kr.mashup.bangwidae.asked.controller.dto

data class QaJoinUserRequest(
    val email: String,
    val password: String,
    val nickname: String?,
    val description: String?,
    val tags: List<String>?,
)