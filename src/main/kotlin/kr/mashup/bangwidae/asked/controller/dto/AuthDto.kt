package kr.mashup.bangwidae.asked.controller.dto

import io.swagger.annotations.ApiModelProperty
import kr.mashup.bangwidae.asked.model.LoginType

// TODO: 하나의 login api로 BASIC, OAUTH 다 커버할지 나눌지?
data class LoginRequest(
    @ApiModelProperty(value = "login id", example = "gardenlee")
    val loginId: String,
    @ApiModelProperty(value = "login password", example = "1q2w3e")
    val loginPassword: String? = null,
    @ApiModelProperty(value = "login type", example = "BASIC, KAKAO, APPLE")
    val loginType: LoginType,
)

data class LoginResponse(
    @ApiModelProperty(value = "access token", example = "eherhljerghjlejrgligjl")
    val accessToken: String
)