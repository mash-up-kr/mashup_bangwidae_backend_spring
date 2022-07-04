package kr.mashup.bangwidae.asked.service.place

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
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

    fun getRepresentativeAddress(longitude: Double, latitude: Double): String {
        val region = reverseGeocode(longitude, latitude)
        return region.군구 ?: region.시 ?: region.읍면동 ?: region.리 ?: region.도
        ?: throw DoriDoriException.of(DoriDoriExceptionType.REPRESENTATIVE_ADDRESS_NOT_EXIST)
    }
}

enum class Nationality {
    KR,
    UNSUPPORTED,
    ;
}