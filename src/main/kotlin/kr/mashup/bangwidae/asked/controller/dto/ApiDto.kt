package kr.mashup.bangwidae.asked.controller.dto

data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val error: ApiErrorResponse?,
)

data class ApiErrorResponse(
    val code: String,
    val message: String?,
)
