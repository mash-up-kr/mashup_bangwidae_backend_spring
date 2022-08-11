package kr.mashup.bangwidae.asked.controller.dto

import kr.mashup.bangwidae.asked.model.Region
import kr.mashup.bangwidae.asked.service.place.Nationality

data class PlaceDto(
    val 국가: Nationality?,
    val 도: String?,
    val 시: String?,
    val 군구: String?,
    val 읍면동: String?,
    val 리: String?,
    val representativeAddress: String,
) {
    companion object {
        fun from(region: Region): PlaceDto {
            return PlaceDto(
                국가 = region.국가,
                도 = region.도,
                시 = region.시,
                군구 = region.군구,
                읍면동 = region.읍면동,
                리 = region.리,
                representativeAddress = region.representativeAddress,
            )
        }
    }
}