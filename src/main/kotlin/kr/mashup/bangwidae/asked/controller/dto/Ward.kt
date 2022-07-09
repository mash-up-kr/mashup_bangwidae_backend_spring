package kr.mashup.bangwidae.asked.controller.dto

import io.swagger.annotations.ApiModelProperty
import kr.mashup.bangwidae.asked.model.Ward
import kr.mashup.bangwidae.asked.utils.getLatitude
import kr.mashup.bangwidae.asked.utils.getLongitude
import java.time.LocalDateTime

data class WardDto(
    @ApiModelProperty(value = "ward id", example = "62b49a12507aeb02e6534572")
    val id: String,
    @ApiModelProperty(value = "와드 이름", example = "도리도리")
    val name: String,
    @ApiModelProperty(value = "경도", example = "127.4")
    val longitude: Double,
    @ApiModelProperty(value = "위도", example = "23.5")
    val latitude: Double,
    @ApiModelProperty(value = "생성일", example = "2022-06-23T16:51:30.717+00:00")
    val createdAt: LocalDateTime?,
    @ApiModelProperty(value = "생성일", example = "2022-06-23T16:51:30.717+00:00")
    val remainDays: String
) {
    companion object {
        fun from(ward: Ward): WardDto {
            return WardDto(
                id = ward.id!!.toHexString(),
                name = ward.name,
                longitude = ward.location.getLongitude(),
                latitude = ward.location.getLatitude(),
                createdAt = ward.createdAt,
                remainDays = ward.getDDays()
            )
        }
    }
}

data class CreateWardRequest(
    @ApiModelProperty(value = "와드이름", example = "내와드")
    val name: String,
    @ApiModelProperty(value = "경도", example = "127.4")
    val longitude: Double,
    @ApiModelProperty(value = "위도", example = "23.5")
    val latitude: Double
)