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

internal class PlaceServiceTest : FunSpec({
    lateinit var naverReverseGeocodeConverter: NaverReverseGeocodeConverter
    lateinit var placeService: PlaceService

    val latitude = 123.456
    val longitude = 78.90

    beforeTest {
        naverReverseGeocodeConverter = mockk()

        placeService = PlaceService(
            naverReverseGeocodeConverter = naverReverseGeocodeConverter,
        )
    }

    afterEach {
        verify {
            naverReverseGeocodeConverter.convert(latitude = latitude, longitude = longitude)
        }
        confirmVerified(naverReverseGeocodeConverter)
    }

    context("위경도를 region 으로 변환 시") {
        test("성공하면 region 을 반환한다.") {
            // given
            val region = Region(
                국가 = Nationality.KR,
                도 = "경기도",
                시 = "용인시",
                군구 = "수지구",
                읍면동 = null,
                리 = null,
            )
            every { naverReverseGeocodeConverter.convert(any(), any()) } returns region

            // when
            val actual = placeService.reverseGeocode(latitude = latitude, longitude = longitude)

            // then
            actual shouldBe region

        }

        test("지원하지 않는 국가면 INVALID_COUNTRY 를 던진다.") {
            // given
            val region = Region(
                국가 = Nationality.UNSUPPORTED,
                도 = null,
                시 = null,
                군구 = null,
                읍면동 = null,
                리 = null,
            )
            every { naverReverseGeocodeConverter.convert(any(), any()) } returns region

            // when
            val exception = shouldThrow<DoriDoriException> {
                placeService.reverseGeocode(latitude = latitude, longitude = longitude)
            }

            // then
            exception.code shouldBe DoriDoriExceptionType.INVALID_COUNTRY.code
        }
    }
})
