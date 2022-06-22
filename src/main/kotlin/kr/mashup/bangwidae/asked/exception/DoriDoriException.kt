package kr.mashup.bangwidae.asked.exception

import kr.mashup.bangwidae.asked.controller.dto.ApiErrorResponse
import org.springframework.http.HttpStatus
import java.lang.RuntimeException

class DoriDoriException(
    val httpStatus: HttpStatus,
    val code: String,
    override val message: String?,
) : RuntimeException() {
    companion object {
        fun of(type: DoriDoriExceptionType): DoriDoriException {
            return DoriDoriException(
                httpStatus = type.httpStatus,
                code = type.code,
                message = type.message,
            )
        }

        fun of(type: DoriDoriExceptionType, message: String?): DoriDoriException {
            return DoriDoriException(
                httpStatus = type.httpStatus,
                code = type.code,
                message = message,
            )
        }
    }

    fun toApiErrorResponse() = ApiErrorResponse(
        code = code,
        message = message,
    )
}
