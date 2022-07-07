package kr.mashup.bangwidae.asked.external.map

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.stereotype.Component

@Component
class NaverMapClient(
    private val naverMapFeignClient: NaverMapFeignClient,
    private val naverMapProperties: NaverMapProperties,
) {
    fun reverseGeocode(longitude: Double, latitude: Double): List<NaverReverseGeocodeResult> {
        val response = naverMapFeignClient.reverseGeocode(
            clientId = naverMapProperties.authorization.clientId,
            clientSecret = naverMapProperties.authorization.clientSecret,
            coordinates = "${longitude},${latitude}",
            responseType = "json"
        )

        // TODO FeignException 을 DoriDoriException 으로

        return response.results!!
    }
}

data class NaverReverseGeocodeResponse(
    val status: NaverReverseGeocodeStatus?,
    val results: List<NaverReverseGeocodeResult>?,
    val error: NaverReverseGeocodeError?,
)

data class NaverReverseGeocodeStatus(
    val code: Int,
    val name: String?,
    val message: String?,
)

data class NaverReverseGeocodeError(
    val errorCode: String,
    val message: String?,
    val details: String?,
)

data class NaverReverseGeocodeResult(
    val name: String?,
    val region: NaverReverseGeocodeRegion?,
)

data class NaverReverseGeocodeRegion(
    // 국가 코드 최상위 도메인 두 자리 (KR)
    @JsonProperty("area0")
    val 국가: NaverReverseGeocodeRegionArea?,
    // 행정안전부에서 공시된 시/도 명칭
    @JsonProperty("area1")
    val 시도: NaverReverseGeocodeRegionArea?,
    // 행정안전부에서 공시된 시/군/구 명칭
    @JsonProperty("area2")
    val 시군구: NaverReverseGeocodeRegionArea?,
    // 행정안전부에서 공시된 읍/면/동 명칭
    @JsonProperty("area3")
    val 읍면동: NaverReverseGeocodeRegionArea?,
    // 행정안전부에서 공시된 리 명칭
    @JsonProperty("area4")
    val 리: NaverReverseGeocodeRegionArea?,
)

data class NaverReverseGeocodeRegionArea(
    val name: String?,
    val coords: NaverReverseGeocodeCoords?,
)

data class NaverReverseGeocodeCoords(
    val center: NaverReverseGeocodeCenter?,
)

data class NaverReverseGeocodeCenter(
    val crs: String?,
    @JsonProperty("x")
    val longitude: Double?,
    @JsonProperty("y")
    val latitude: Double?,
)

