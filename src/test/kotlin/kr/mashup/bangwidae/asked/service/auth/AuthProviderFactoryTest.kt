package kr.mashup.bangwidae.asked.service.auth

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.kotest.matchers.types.beInstanceOf
import io.mockk.mockk
import kr.mashup.bangwidae.asked.external.auth.AppleOauthProvider
import kr.mashup.bangwidae.asked.external.auth.KakaoOauthProvider
import kr.mashup.bangwidae.asked.model.LoginType

internal class AuthProviderFactoryTest: DescribeSpec({
    describe("AuthProviderFactory 에서") {
        val factory = AuthProviderFactory(mockk(), mockk())
        context("APPLE TYPE 을 넘긴 경우") {
            val result = factory.findBy(LoginType.APPLE)
            it("AppleOauthProvider 를 반환한다.") {
                result should beInstanceOf<AppleOauthProvider>()
            }
        }

        context("KAKAO TYPE 을 넘긴 경우") {
            val result = factory.findBy(LoginType.KAKAO)
            it("KakaoOauthProvider를 반환한다.") {
                result should beInstanceOf<KakaoOauthProvider>()
            }
        }
    }
})
