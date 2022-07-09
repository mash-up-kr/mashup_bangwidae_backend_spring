package kr.mashup.bangwidae.asked.external.map

import feign.FeignException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType

internal class NaverMapClientTest : FunSpec({
    lateinit var naverMapFeignClient: NaverMapFeignClient
    lateinit var naverMapProperties: NaverMapProperties
    lateinit var naverMapClient: NaverMapClient

    val latitude = 123.456
    val longitude = 78.90
    val clientId = "clientId"
    val clientSecret = "clientSecret"

    beforeTest {
        naverMapFeignClient = mockk()
        naverMapProperties = mockk()

        naverMapClient = NaverMapClient(
            naverMapFeignClient = naverMapFeignClient,
            naverMapProperties = naverMapProperties,
        )
    }

    afterEach {
        verify {
            naverMapFeignClient.reverseGeocode(
                clientId = clientId,
                clientSecret = clientSecret,
                coordinates = "${longitude},${latitude}",
                responseType = "json",
            )
        }
        confirmVerified(naverMapFeignClient)
    }

    context("Naver Reverse Geocode API 호출 시") {
        test("성공하면 results를 반환한다.") {
            // given
            val results = listOf(mockk<NaverReverseGeocodeResult>())
            val response = NaverReverseGeocodeResponse(
                status = mockk(),
                results = results,
                error = mockk(),
            )
            every { naverMapFeignClient.reverseGeocode(any(), any(), any(), any()) } returns response
            every { naverMapProperties.authorization.clientId } returns clientId
            every { naverMapProperties.authorization.clientSecret } returns clientSecret

            // when
            val actual = naverMapClient.reverseGeocode(latitude = latitude, longitude = longitude)

            // then
            actual shouldBe results
        }

        test("FeignException이 발생하면 PLACE_FETCH_FAIL 을 던진다.") {
            // given
            every { naverMapFeignClient.reverseGeocode(any(), any(), any(), any()) } throws FeignException.BadRequest(
                "test message", mockk(), null
            )
            every { naverMapProperties.authorization.clientId } returns clientId
            every { naverMapProperties.authorization.clientSecret } returns clientSecret

            // when
            val exception = shouldThrow<DoriDoriException> {
                naverMapClient.reverseGeocode(latitude = latitude, longitude = longitude)
            }

            // then
            exception.code shouldBe DoriDoriExceptionType.PLACE_FETCH_FAIL.code
            exception.message shouldBe "test message"
        }
    }
})
