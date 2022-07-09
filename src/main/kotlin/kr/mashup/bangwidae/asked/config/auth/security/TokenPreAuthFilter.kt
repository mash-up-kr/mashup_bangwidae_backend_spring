package kr.mashup.bangwidae.asked.config.auth.security

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import kr.mashup.bangwidae.asked.config.auth.jwt.JwtService
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.service.UserService
import org.bson.types.ObjectId
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import org.springframework.util.StringUtils
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest


class TokenPreAuthFilter(
    private val jwtService: JwtService,
    private val userService: UserService,
) : AbstractPreAuthenticatedProcessingFilter() {
    override fun getPreAuthenticatedPrincipal(request: HttpServletRequest): Any? {
        val token: String = resolveToken(request)?: return null
        val user = runCatching {
            jwtService.decodeToken(token)?.let {
                userService.findById(ObjectId(it))
            }
        }.onFailure {
            when(it) {
                is TokenExpiredException -> request.setAttribute("error", DoriDoriExceptionType.TOKEN_EXPIRED)
                is JWTVerificationException -> request.setAttribute("error", DoriDoriExceptionType.AUTHENTICATED_FAIL)
            }
        }.getOrNull()?: return null

        return user
    }
    override fun getPreAuthenticatedCredentials(request: HttpServletRequest): Any? {
        return resolveToken(request)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val token = request.getHeader(AUTHORIZATION_HEADER_NAME)
        if (!StringUtils.hasText(token)) {
            return null
        }
        val matcher: Matcher = BEARER_TOKEN_PATTERN.matcher(token)
        return if (!matcher.matches()) {
            null
        } else {
            matcher.group(1)
        }
    }

    companion object {
        private const val AUTHORIZATION_HEADER_NAME = "Authorization"
        private val BEARER_TOKEN_PATTERN: Pattern = Pattern.compile("[Bb]earer (.*)")
    }
}