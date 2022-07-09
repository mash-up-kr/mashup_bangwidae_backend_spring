package kr.mashup.bangwidae.asked.controller.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ApiErrorResponse? = null,
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(
                success = true,
                data = data,
            )
        }

        fun fail(error: ApiErrorResponse): ApiResponse<Unit> {
            return ApiResponse(
                success = false,
                error = error
            )
        }
    }
}

data class ApiErrorResponse(
    val code: String,
    val message: String?,
)
