package kr.mashup.bangwidae.asked.service.place

interface ReverseGeocodeConverter {
    fun convert(longitude: Double, latitude: Double): Region
}

data class Region(
    val 국가: String?,
    val 시도: String?,
    val 시군구: String?,
    val 읍면동: String?,
    val 리: String?,
)

data class RegionDetail(
    val 국가: String?,
    val 도: String?,
    val 시: String?,
    val 군구: String?,
    val 읍면동: String?,
    val 리: String?,
)