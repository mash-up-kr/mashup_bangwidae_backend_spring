package kr.mashup.bangwidae.asked.external.auth

import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.service.auth.AuthProvider
import org.springframework.stereotype.Service

@Service
class AppleOauthProvider: AuthProvider {
    override fun socialLogin(user: User) {
        TODO("Not yet implemented")
    }
}