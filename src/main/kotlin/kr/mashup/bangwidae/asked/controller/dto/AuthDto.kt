package kr.mashup.bangwidae.asked.controller.dto

import io.swagger.annotations.ApiModelProperty
import kr.mashup.bangwidae.asked.model.LoginType

// TODO: 하나의 login api로 BASIC, OAUTH 다 커버할지 나눌지?
data class LoginRequest(
    @ApiModelProperty(value = "이메일", example = "doridori@gmail.com")
    val email: String,
    @ApiModelProperty(value = "login password", example = "1q2w3e")
    val loginPassword: String? = null,
    @ApiModelProperty(value = "login type", example = "BASIC, KAKAO, APPLE")
    val loginType: LoginType,
)

data class LoginResponse(
    @ApiModelProperty(value = "access token", example = "eherhljerghjlejrgligjl")
    val accessToken: String
)

data class CertMailSendRequest(
    @ApiModelProperty(value = "이메일", example = "doridori@gmail.com")
    val email: String
)

data class CertMailRequest(
    @ApiModelProperty(value = "이메일", example = "doridori@gmail.com")
    val email: String,
    @ApiModelProperty(value = "인증 번호 (6자리 숫자)", example = "000000")
    val certificationNumber: String,
)