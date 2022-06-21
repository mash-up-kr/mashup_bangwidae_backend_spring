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

    // TODO : expired 결정, refresh 할건지? 등등
    fun createAccessToken(userId: String): String {
        return JWT.create()
            .withIssuer(tokenIssuer)
            .withClaim(CLAIM_NAME, userId)
            .sign(algorithm)
    }

    fun decodeToken(token: String?): String? {
        return try {
            // claim 에 string 을 처음 넣어봤는데 string 에 " 가 붙는 기이한 현상 발생해서 replace 하는 야매 사용 ;;
            jwtVerifier.verify(token).let { it.claims[CLAIM_NAME]?.toString()?.replace("\"", "") }
        } catch (ex: JWTVerificationException) {
            null
        }
    }

    companion object {
        private const val CLAIM_NAME = "userId"
    }
}