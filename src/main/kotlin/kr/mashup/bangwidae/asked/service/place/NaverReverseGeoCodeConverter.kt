package kr.mashup.bangwidae.asked.service.place

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.external.map.NaverMapClient
import org.springframework.stereotype.Component

@Component
class NaverReverseGeocodeConverter(
    private val naverMapClient: NaverMapClient,
) : ReverseGeocodeConverter {
    override fun convert(longitude: Double, latitude: Double): Region {
        val reverseGeocodeResults = naverMapClient.reverseGeocode(
            longitude = longitude,
            latitude = latitude,
        )

        val naverRegion = reverseGeocodeResults
            .firstOrNull()
            ?.region
            ?.let {
                NaverRegion(
                    국가 = it.area0?.name,
                    시도 = it.area1?.name,
                    시군구 = it.area2?.name,
                    읍면동 = it.area3?.name,
                    리 = it.area4?.name
                )
            } ?: throw DoriDoriException.of(
            type = DoriDoriExceptionType.PLACE_FETCH_FAIL,
            message = "위치 정보가 없어요"
        )

        return naverRegion.toRegion()
    }

    private fun NaverRegion.toRegion(): Region {
        return Region(
            국가 = when (국가) {
                "kr" -> Nationality.KR
                else -> Nationality.UNSUPPORTED
            },
            도 = 시도?.takeIf { it.endsWith("도") },
            시 = 시도?.takeIf { it.endsWith("시") }
                ?: 시군구?.split(" ")?.firstOrNull { it.endsWith("시") },
            군구 = 시군구?.split(" ")?.firstOrNull { it.endsWith("군") || it.endsWith("구") },
            읍면동 = 읍면동,
            리 = 리,
        )
    }
}

data class NaverRegion(
    val 국가: String?,
    val 시도: String?,
    val 시군구: String?,
    val 읍면동: String?,
    val 리: String?,
)
