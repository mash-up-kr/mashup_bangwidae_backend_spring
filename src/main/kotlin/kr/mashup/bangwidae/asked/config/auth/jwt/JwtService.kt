package kr.mashup.bangwidae.asked.config.auth.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class JwtService(
    @Value("\${jwt.token-issuer}")
    private val tokenIssuer: String,
    @Value("\${jwt.secret-key}")
    private val tokenSigningKey: String
) {
    private val algorithm: Algorithm = Algorithm.HMAC256(tokenSigningKey)
    private val jwtVerifier: JWTVerifier = JWT.require(algorithm).build()

    fun encode(userId: String): String {
        return JWT.create()
            .withIssuer(tokenIssuer)
            .withClaim("userId", userId)
            .sign(algorithm)
    }

    fun decode(token: String?): Long? {
        return try {
            jwtVerifier.verify(token).let { it.claims["userId"]?.asLong() }
        } catch (ex: JWTVerificationException) {
            null
        }
    }
}