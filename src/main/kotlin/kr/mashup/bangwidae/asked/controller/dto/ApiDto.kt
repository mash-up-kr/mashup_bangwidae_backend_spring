package kr.mashup.bangwidae.asked.controller.dto

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