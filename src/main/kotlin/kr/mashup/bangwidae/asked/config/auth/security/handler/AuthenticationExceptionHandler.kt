package kr.mashup.bangwidae.asked.config.auth.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import kr.mashup.bangwidae.asked.controller.dto.ApiErrorResponse
import kr.mashup.bangwidae.asked.controller.dto.ApiResponse
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationExceptionHandler(
    private val objectMapper: ObjectMapper,
): AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        try {
            val error = request?.getAttribute("error") as DoriDoriExceptionType

            response?.status = HttpStatus.UNAUTHORIZED.value()
            response?.contentType = MediaType.APPLICATION_JSON_VALUE

            if (error == DoriDoriExceptionType.TOKEN_EXPIRED) {
                objectMapper.writeValue(
                    response?.outputStream,
                    ApiResponse.fail(
                        ApiErrorResponse(
                            code = DoriDoriExceptionType.TOKEN_EXPIRED.name,
                            message = DoriDoriExceptionType.TOKEN_EXPIRED.message
                        )
                    )
                )
            } else {
                objectMapper.writeValue(
                    response?.outputStream,
                    ApiResponse.fail(
                        ApiErrorResponse(
                            code = DoriDoriExceptionType.AUTHENTICATED_FAIL.name,
                            message = DoriDoriExceptionType.AUTHENTICATED_FAIL.message
                        )
                    )
                )
            }
        } catch (e: Exception) {
            response?.status = HttpStatus.UNAUTHORIZED.value()
            response?.contentType = MediaType.APPLICATION_JSON_VALUE

            objectMapper.writeValue(
                response?.outputStream,
                ApiResponse.fail(
                    ApiErrorResponse(
                        code = DoriDoriExceptionType.AUTHENTICATED_FAIL.name,
                        message = DoriDoriExceptionType.AUTHENTICATED_FAIL.message
                    )
                )
            )
        }
    }
}
