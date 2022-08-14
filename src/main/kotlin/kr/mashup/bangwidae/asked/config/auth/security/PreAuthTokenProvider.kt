package kr.mashup.bangwidae.asked.config.auth.security

import kr.mashup.bangwidae.asked.model.document.User
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import java.util.*


class PreAuthTokenProvider : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication? {
        if (authentication is PreAuthenticatedAuthenticationToken) {
            val user = authentication.getPrincipal() as User
            return PreAuthenticatedAuthenticationToken(
                user,
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