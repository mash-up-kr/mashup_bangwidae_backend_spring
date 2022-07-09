package kr.mashup.bangwidae.asked.config.auth.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(
    @Value("\${jwt.token-issuer}")
    private val tokenIssuer: String,
    @Value("\${jwt.secret-key}")
    private val tokenSigningKey: String
) {
    private val algorithm: Algorithm = Algorithm.HMAC256(tokenSigningKey)
    private val jwtVerifier: JWTVerifier = JWT.require(algorithm).build()

    fun createAccessToken(userId: String): String {
        return JWT.create()
            .withExpiresAt(accessTokenExpired())
            .withIssuer(tokenIssuer)
            .withClaim(CLAIM_NAME, userId)
            .sign(algorithm)
    }

    fun createRefreshToken(userId: String): String {
        return JWT.create()
            .withExpiresAt(refreshTokenExpired())
            .withIssuer(tokenIssuer)
            .withClaim(CLAIM_NAME, userId)
            .sign(algorithm)
    }

    fun decodeToken(token: String?): String? {
        return try {
            // claim 에 string 을 처음 넣어봤는데 string 에 " 가 붙는 기이한 현상 발생해서 replace 하는 야매 사용 ;;
            jwtVerifier.verify(token).let { it.claims[CLAIM_NAME]?.toString()?.replace("\"", "") }
        } catch (ex: JWTVerificationException) {
            throw ex
        } catch (ex: TokenExpiredException) {
            throw ex
        }
    }

    private fun accessTokenExpired(): Date {
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.HOUR, 1)
        return cal.time
    }

    private fun refreshTokenExpired(): Date {
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.HOUR, 24 * 30 * 6)
        return cal.time
    }


    companion object {
        private const val CLAIM_NAME = "userId"
    }
}