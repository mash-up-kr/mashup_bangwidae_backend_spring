package kr.mashup.bangwidae.asked.model

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.service.place.Nationality

data class Region(
    val 국가: Nationality?,
    val 도: String?,
    val 시: String?,
    val 군구: String?,
    val 읍면동: String?,
    val 리: String?,
) {
    val representativeAddress: String
        get() = 군구 ?: 시 ?: 읍면동 ?: 리 ?: 도
        ?: throw DoriDoriException.of(DoriDoriExceptionType.REPRESENTATIVE_ADDRESS_NOT_EXIST)
}