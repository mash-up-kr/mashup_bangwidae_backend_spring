package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.ApiResponse
import kr.mashup.bangwidae.asked.controller.dto.LoginRequest
import kr.mashup.bangwidae.asked.controller.dto.LoginResponse
import kr.mashup.bangwidae.asked.controller.dto.MailAuthRequest
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
        @RequestBody mailAuthRequest: MailAuthRequest
    ): ApiResponse<Boolean> {
        authService.sendCertMail(mailAuthRequest)
        return ApiResponse.success(true)
    }

}