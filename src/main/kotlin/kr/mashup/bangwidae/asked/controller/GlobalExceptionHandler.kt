package kr.mashup.bangwidae.asked.controller

import kr.mashup.bangwidae.asked.controller.dto.ApiErrorResponse
import kr.mashup.bangwidae.asked.controller.dto.ApiResponse
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<ApiResponse<Any>> {
        logger.error { exception.printStackTrace() }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ApiResponse(
                    success = false,
                    data = null,
                    error = ApiErrorResponse(
                        code = DoriDoriExceptionType.FAIL.name,
                        message = exception.message,
                    ),
                )
            )
    }

    @ExceptionHandler(DoriDoriException::class)
    fun handleDoriDoriException(exception: DoriDoriException): ResponseEntity<ApiResponse<Any>> {
        logger.error { exception.printStackTrace() }
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                ApiResponse(
                    success = false,
                    data = null,
                    error = exception.toApiErrorResponse(),
                )
            )
    }
}
