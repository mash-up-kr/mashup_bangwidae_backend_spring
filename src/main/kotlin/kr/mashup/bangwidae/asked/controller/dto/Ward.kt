package kr.mashup.bangwidae.asked.controller.dto

import kr.mashup.bangwidae.asked.model.document.Ward
import kr.mashup.bangwidae.asked.utils.getLatitude
import kr.mashup.bangwidae.asked.utils.getLongitude
import java.time.LocalDateTime

data class WardDto(
    val id: String,
    val name: String,
    val longitude: Double,
    val latitude: Double,
    val city: String,
    val createdAt: LocalDateTime?,
    val remainDays: String,
    val isRepresentative: Boolean
) {
    companion object {
        fun from(ward: Ward): WardDto {
            return WardDto(
                id = ward.id!!.toHexString(),
                name = ward.name,
                longitude = ward.location.getLongitude(),
                latitude = ward.location.getLatitude(),
                createdAt = ward.createdAt,
                remainDays = ward.getDDays(),
                city = ward.city,
                isRepresentative = ward.isRepresentative ?: false
            )
        }
    }
}

data class CreateWardRequest(
    val name: String,
    val longitude: Double,
    val latitude: Double
)

data class ExtendWardPeriodRequest(
    val period: Int,
)