package kr.mashup.bangwidae.asked.service.auth

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.external.auth.AppleOauthProvider
import kr.mashup.bangwidae.asked.external.auth.KakaoOauthProvider
import kr.mashup.bangwidae.asked.model.LoginType
import org.springframework.stereotype.Component

@Component
class AuthProviderFactory(
    private val appleOauthProvider: AppleOauthProvider,
    private val kakaoOauthProvider: KakaoOauthProvider
) {
    fun findBy(loginType: LoginType): AuthProvider {
        return when(loginType) {
            LoginType.APPLE -> appleOauthProvider
            LoginType.KAKAO -> kakaoOauthProvider
            else -> throw DoriDoriException.of(DoriDoriExceptionType.COMMON_ERROR)
        }
    }
}