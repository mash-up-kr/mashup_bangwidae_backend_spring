package kr.mashup.bangwidae.asked.config.auth.security

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import java.util.*


class PreAuthTokenProvider(
    private val jwtService: Any
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication? {
        if (authentication is PreAuthenticatedAuthenticationToken) {
            val token: String = authentication.getPrincipal().toString()
            return PreAuthenticatedAuthenticationToken(
                "userId",
                "",
                Collections.singletonList(SimpleGrantedAuthority("ROLE"))
            )
        }
        return null
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return PreAuthenticatedAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}