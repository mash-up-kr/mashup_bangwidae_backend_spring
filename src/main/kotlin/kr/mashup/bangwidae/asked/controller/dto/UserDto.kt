package kr.mashup.bangwidae.asked.controller.dto

import io.swagger.annotations.ApiModelProperty
import kr.mashup.bangwidae.asked.model.User

data class UserDto(
    @ApiModelProperty(value = "user id", example = "gardenlee")
    val id: String,
    @ApiModelProperty(value = "user nickname", example = "gardenlee")
    val nickname: String
) {
    companion object {
        fun from(user: User): UserDto {
            return UserDto(
                id = user.id!!.toHexString(),
                nickname = user.nickname
            )
        }
    }
}