package kr.mashup.bangwidae.asked.service.auth

import kr.mashup.bangwidae.asked.model.document.User

interface AuthProvider {
    fun socialLogin(user: User)
}