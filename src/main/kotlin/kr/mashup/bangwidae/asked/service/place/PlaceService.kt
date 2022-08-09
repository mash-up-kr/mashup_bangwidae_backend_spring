package kr.mashup.bangwidae.asked.service.place

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.Region
import org.springframework.stereotype.Service

@Service
class PlaceService(
    private val naverReverseGeocodeConverter: NaverReverseGeocodeConverter
) {
    fun reverseGeocode(longitude: Double, latitude: Double): Region {
        val region = naverReverseGeocodeConverter.convert(
            longitude = longitude,
            latitude = latitude,
        )

        if (region.국가 === Nationality.UNSUPPORTED) {
            throw DoriDoriException.of(DoriDoriExceptionType.INVALID_COUNTRY)
        }

        return region
    }
}

enum class Nationality {
    KR,
    UNSUPPORTED,
    ;
}