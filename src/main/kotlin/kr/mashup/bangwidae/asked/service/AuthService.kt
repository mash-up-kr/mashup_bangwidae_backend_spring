package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.config.auth.jwt.JwtService
import kr.mashup.bangwidae.asked.controller.dto.LoginRequest
import kr.mashup.bangwidae.asked.controller.dto.LoginResponse
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class AuthService(
    private val jwtService: JwtService,
    private val userService: UserService
) {
    fun login(loginRequest: LoginRequest): LoginResponse {
        val user = (userService.findByLoginId(loginRequest.loginId)
            ?: throw IllegalArgumentException("유저가 없어용"))

        // TODO 암호화된 패스워드 매치
        if (user.password != loginRequest.loginPassword)
            throw IllegalArgumentException("비밀번호가 틀려용")

        return LoginResponse(
            accessToken = jwtService.createAccessToken(user.id!!.toHexString())
        )
    }
}