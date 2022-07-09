package kr.mashup.bangwidae.asked.exception

import kr.mashup.bangwidae.asked.controller.dto.ApiErrorResponse
import org.springframework.http.HttpStatus
import java.lang.RuntimeException

class DoriDoriException(
    val code: String,
    override val message: String? = null,
) : RuntimeException() {
    companion object {
        fun of(type: DoriDoriExceptionType): DoriDoriException {
            return DoriDoriException(
                code = type.name,
                message = type.message,
            )
        }

        fun of(type: DoriDoriExceptionType, message: String?): DoriDoriException {
            return DoriDoriException(
                code = type.name,
                message = message,
            )
        }
    }

    fun toApiErrorResponse() = ApiErrorResponse(
        code = code,
        message = message,
    )
}
