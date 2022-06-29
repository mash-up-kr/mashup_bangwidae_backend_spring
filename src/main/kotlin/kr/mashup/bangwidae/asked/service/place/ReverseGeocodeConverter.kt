package kr.mashup.bangwidae.asked.service.place

interface ReverseGeocodeConverter {
    fun convert(longitude: Double, latitude: Double): Region
}

data class Region(
    val 국가: Nationality?,
    val 도: String?,
    val 시: String?,
    val 군구: String?,
    val 읍면동: String?,
    val 리: String?,
)