package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.*
import kr.mashup.bangwidae.asked.service.auth.AuthService
import org.springframework.web.bind.annotation.*

@Api(tags = ["인증 컨트롤러"])
@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {

    @ApiOperation("로그인")
    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest
    ): ApiResponse<LoginResponse> {
        return ApiResponse.success(authService.login(loginRequest))
    }

    @ApiOperation("인증메일 발송")
    @PostMapping("/mail/send")
    fun sendCertMail(
        @RequestBody certMailSendRequest: CertMailSendRequest
    ): ApiResponse<Boolean> {
        authService.sendCertMail(certMailSendRequest)
        return ApiResponse.success(true)
    }

    @ApiOperation("메일 인증")
    @PostMapping("/mail/cert")
    fun sendCertMail(
        @RequestBody certMailRequest: CertMailRequest
    ): ApiResponse<Boolean> {
        authService.certMail(certMailRequest)
        return ApiResponse.success(true)
    }

}