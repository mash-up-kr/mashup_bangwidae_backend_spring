package kr.mashup.bangwidae.asked.config.auth.password

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.regex.Pattern

@Service
class PasswordService(
    private val passwordEncoder: PasswordEncoder
) {

    fun encodePassword(password: String): String {
        return passwordEncoder.encode(password)
    }

    fun matchPassword(password: String, encodedPassword: String) {
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw DoriDoriException.of(DoriDoriExceptionType.INVALID_PASSWORD)
        }
    }

    fun validatePassword(password: String) {
        val pattern = Pattern.compile(VALID_PASSWORD_REGEX)

        if (!pattern.matcher(password).matches()) {
            throw DoriDoriException.of(DoriDoriExceptionType.INVALID_PASSWORD_REGEX)
        }

        if (password.length < 6) {
            throw DoriDoriException.of(DoriDoriExceptionType.INVALID_PASSWORD_LENGTH)
        }
    }

    companion object {
        const val VALID_PASSWORD_REGEX = """^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^+\-=])(?=\S+$).*$"""
    }

}
