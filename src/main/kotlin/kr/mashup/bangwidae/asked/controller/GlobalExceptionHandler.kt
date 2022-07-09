package kr.mashup.bangwidae.asked.controller

import kr.mashup.bangwidae.asked.controller.dto.ApiResponse
import kr.mashup.bangwidae.asked.controller.dto.ApiErrorResponse
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<ApiResponse<Any>> {
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
