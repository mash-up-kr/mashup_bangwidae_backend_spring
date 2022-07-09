package kr.mashup.bangwidae.asked.controller.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val error: ApiErrorResponse? = null,
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse<T>(
                success = true,
                data = data,
            )
        }
    }
}

data class ApiErrorResponse(
    val code: String,
    val message: String?,
)
