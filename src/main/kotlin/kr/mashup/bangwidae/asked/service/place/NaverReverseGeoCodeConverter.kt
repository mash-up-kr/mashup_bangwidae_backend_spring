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

        return reverseGeocodeResults
            .takeIf { it.isNotEmpty() }
            ?.first()
            ?.region
            ?.let {
                Region(
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
    }
}
