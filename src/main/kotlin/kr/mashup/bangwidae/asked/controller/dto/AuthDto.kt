package kr.mashup.bangwidae.asked.controller.dto

import io.swagger.annotations.ApiModelProperty
import kr.mashup.bangwidae.asked.model.LoginType

data class LoginRequest(
    @ApiModelProperty(value = "login id", example = "gardenlee")
    val loginId: String,
    @ApiModelProperty(value = "login password", example = "1q2w3e")
    val loginPassword: String? = null,
    @ApiModelProperty(value = "login type", example = "BASIC, KAKAO, APPLE")
    val loginType: LoginType,
)

data class LoginResponse(
    @ApiModelProperty(value = "token", example = "eherhljerghjlejrgligjl")
    val token: String
)