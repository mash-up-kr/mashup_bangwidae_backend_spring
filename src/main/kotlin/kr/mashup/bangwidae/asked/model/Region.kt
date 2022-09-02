package kr.mashup.bangwidae.asked.model

import kr.mashup.bangwidae.asked.service.place.Nationality

data class Region(
    val 국가: Nationality? = null,
    val 도: String? = null,
    val 시: String? = null,
    val 군구: String? = null,
    val 읍면동: String? = null,
    val 리: String? = null,
) {
    val representativeAddress: String
        get() = 군구 ?: 시 ?: 읍면동 ?: 리 ?: 도 ?: ""
}