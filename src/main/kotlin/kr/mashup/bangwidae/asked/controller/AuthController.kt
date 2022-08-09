package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.*
import kr.mashup.bangwidae.asked.controller.path.ApiPath
import kr.mashup.bangwidae.asked.service.auth.AuthService
import org.springframework.web.bind.annotation.*

@Api(tags = ["인증 컨트롤러"])
@RestController
@RequestMapping(ApiPath.ROOT)
class AuthController(
    private val authService: AuthService
) {

    @ApiOperation("로그인")
    @PostMapping(ApiPath.LOGIN)
    fun login(
        @RequestBody loginRequest: LoginRequest
    ): ApiResponse<LoginResponse> {
        return ApiResponse.success(authService.login(loginRequest))
    }

    @ApiOperation("인증메일 발송")
    @PostMapping(ApiPath.CERT_MAIL_SEND)
    fun sendCertMail(
        @RequestBody certMailSendRequest: CertMailSendRequest
    ): ApiResponse<Boolean> {
        authService.sendCertMail(certMailSendRequest)
        return ApiResponse.success(true)
    }

    @ApiOperation("메일 인증")
    @PostMapping(ApiPath.CERT_MAIL)
    fun sendCertMail(
        @RequestBody certMailRequest: CertMailRequest
    ): ApiResponse<Boolean> {
        authService.certMail(certMailRequest)
        return ApiResponse.success(true)
    }

    @ApiOperation("토큰 재발급")
    @PostMapping(ApiPath.ISSUE_TOKEN)
    fun issueToken(
        @RequestBody issueTokenRequest: IssueTokenRequest
    ): ApiResponse<IssueTokenResponse> {
        return ApiResponse.success(authService.issueToken(issueTokenRequest.refreshToken))
    }

}