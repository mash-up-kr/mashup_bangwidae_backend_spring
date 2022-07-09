package kr.mashup.bangwidae.asked.service.place

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.external.map.NaverMapClient
import kr.mashup.bangwidae.asked.external.map.NaverReverseGeocodeRegion
import kr.mashup.bangwidae.asked.external.map.NaverReverseGeocodeRegionArea
import kr.mashup.bangwidae.asked.external.map.NaverReverseGeocodeResult

internal class NaverReverseGeocodeConverterTest : FunSpec({
    lateinit var naverMapClient: NaverMapClient
    lateinit var naverReverseGeocodeConverter: NaverReverseGeocodeConverter

    val latitude = 123.456
    val longitude = 78.90

    beforeTest {
        naverMapClient = mockk()

        naverReverseGeocodeConverter = NaverReverseGeocodeConverter(
            naverMapClient = naverMapClient,
        )
    }

    afterEach {
        verify {
            naverMapClient.reverseGeocode(latitude = latitude, longitude = longitude)
        }
        confirmVerified(naverMapClient)
    }

    fun generateNaverReverseGeocodeResults(
        국가: String? = "kr",
        시도: String? = null,
        시군구: String? = null,
        읍면동: String? = null,
        리: String? = null,
    ): List<NaverReverseGeocodeResult> {
        return listOf(
            NaverReverseGeocodeResult(
                name = "name",
                region = NaverReverseGeocodeRegion(
                    국가 = NaverReverseGeocodeRegionArea(name = 국가, coords = mockk()),
                    시도 = NaverReverseGeocodeRegionArea(name = 시도, coords = mockk()),
                    시군구 = NaverReverseGeocodeRegionArea(name = 시군구, coords = mockk()),
                    읍면동 = NaverReverseGeocodeRegionArea(name = 읍면동, coords = mockk()),
                    리 = NaverReverseGeocodeRegionArea(name = 리, coords = mockk()),
                ),
            )
        )
    }

    context("위경도를 region 으로 변환") {
        test("도 정보 없이 시 정보가 있는 경우 - 서울특별시 강남구") {
            // given
            val results = generateNaverReverseGeocodeResults(
                시도 = "서울특별시",
                시군구 = "서초구",
                읍면동 = "서초동",
            )
            every { naverMapClient.reverseGeocode(any(), any()) } returns results

            // when
            val actual = naverReverseGeocodeConverter.convert(latitude = latitude, longitude = longitude)

            // then
            actual shouldBe Region(
                국가 = Nationality.KR,
                도 = null,
                시 = "서울특별시",
                군구 = "서초구",
                읍면동 = "서초동",
                리 = null,
            )
        }

        test("도, 시, 구 정보가 모두 있는 경우 - 경기도 용인시 수지구") {
            // given
            val results = generateNaverReverseGeocodeResults(
                시도 = "경기도",
                시군구 = "용인시 수지구",
                읍면동 = "동천동",
            )
            every { naverMapClient.reverseGeocode(any(), any()) } returns results

            // when
            val actual = naverReverseGeocodeConverter.convert(latitude = latitude, longitude = longitude)

            // then
            actual shouldBe Region(
                국가 = Nationality.KR,
                도 = "경기도",
                시 = "용인시",
                군구 = "수지구",
                읍면동 = "동천동",
                리 = null,
            )
        }

        test("구 정보 없이 리 정보가 경우 - 경상북도 상주시 사벌국면 매호리") {
            // given
            val results = generateNaverReverseGeocodeResults(
                시도 = "경상북도",
                시군구 = "상주시",
                읍면동 = "사벌국면",
                리 = "매호리"
            )
            every { naverMapClient.reverseGeocode(any(), any()) } returns results

            // when
            val actual = naverReverseGeocodeConverter.convert(latitude = latitude, longitude = longitude)

            // then
            actual shouldBe Region(
                국가 = Nationality.KR,
                도 = "경상북도",
                시 = "상주시",
                군구 = null,
                읍면동 = "사벌국면",
                리 = "매호리",
            )
        }

        test("한국이 아닌 경우 - 일본 도쿄") {
            // given
            val results = generateNaverReverseGeocodeResults(
                국가 = "jp"
            )
            every { naverMapClient.reverseGeocode(any(), any()) } returns results

            // when
            val actual = naverReverseGeocodeConverter.convert(latitude = latitude, longitude = longitude)

            // then
            actual shouldBe Region(
                국가 = Nationality.UNSUPPORTED,
                도 = null,
                시 = null,
                군구 = null,
                읍면동 = null,
                리 = null,
            )
        }

        test("결과가 빈 리스트이면 PLACE_FETCH_FAIL 을 던진다.") {
            // given
            every { naverMapClient.reverseGeocode(any(), any()) } returns emptyList()

            // when
            val exception = shouldThrow<DoriDoriException> {
                naverReverseGeocodeConverter.convert(latitude = latitude, longitude = longitude)
            }

            // then
            exception.code shouldBe DoriDoriExceptionType.PLACE_FETCH_FAIL.code
        }

        test("리스트 첫 원소의 region 이 없으면 PLACE_FETCH_FAIL 을 던진다.") {
            // given
            every { naverMapClient.reverseGeocode(any(), any()) } returns listOf(
                NaverReverseGeocodeResult(
                    name = null,
                    region = null,
                )
            )

            // when
            val exception = shouldThrow<DoriDoriException> {
                naverReverseGeocodeConverter.convert(latitude = latitude, longitude = longitude)
            }

            // then
            exception.code shouldBe DoriDoriExceptionType.PLACE_FETCH_FAIL.code
        }
    }
})
