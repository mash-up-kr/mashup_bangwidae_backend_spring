package kr.mashup.bangwidae.asked.config.auth.security

import kr.mashup.bangwidae.asked.config.auth.jwt.JwtService
import kr.mashup.bangwidae.asked.service.UserService
import org.bson.types.ObjectId
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import java.util.*


class PreAuthTokenProvider(
    private val jwtService: JwtService,
    private val userService: UserService
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication? {
        if (authentication is PreAuthenticatedAuthenticationToken) {
            val token: String = authentication.getPrincipal().toString()
            val userId = ObjectId(jwtService.decodeToken(token))
            val user = userService.findById(userId)
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