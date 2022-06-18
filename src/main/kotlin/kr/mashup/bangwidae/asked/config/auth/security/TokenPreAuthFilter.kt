package kr.mashup.bangwidae.asked.config.auth.security

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import org.springframework.util.StringUtils
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest


class TokenPreAuthFilter : AbstractPreAuthenticatedProcessingFilter() {
    override fun getPreAuthenticatedPrincipal(request: HttpServletRequest): Any? {
        return resolveToken(request)
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