package kr.mashup.bangwidae.asked.jwt

import kr.mashup.bangwidae.asked.config.auth.jwt.JwtService
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JwtServiceTests(
    @Autowired
    val jwtService: JwtService
) {
    @Test
    @DisplayName("jwt encode & decode test")
    fun jwtEncodeDecodeTest() {
        //given
        val oid = ObjectId().toHexString()

        //when
        val token = jwtService.createAccessToken(oid)
        val decodedOid = jwtService.decodeToken(token)

        //then
        assertThat(oid).isEqualTo(decodedOid)
    }
}