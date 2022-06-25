package kr.mashup.bangwidae.asked.service.auth

import kr.mashup.bangwidae.asked.config.auth.jwt.JwtService
import kr.mashup.bangwidae.asked.config.auth.password.PasswordService
import kr.mashup.bangwidae.asked.controller.dto.LoginRequest
import kr.mashup.bangwidae.asked.controller.dto.LoginResponse
import kr.mashup.bangwidae.asked.controller.dto.MailAuthRequest
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.external.mail.GmailSender
import kr.mashup.bangwidae.asked.model.LoginType
import kr.mashup.bangwidae.asked.model.MailTemplate
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.service.CertMailService
import kr.mashup.bangwidae.asked.service.UserService
import mu.KotlinLogging
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

private val logger = KotlinLogging.logger {}

@Service
class AuthService(
    private val gmailSender: GmailSender,

    private val jwtService: JwtService,
    private val userService: UserService,
    private val certMailService: CertMailService,
    private val passwordService: PasswordService,
    private val authProviderFactory: AuthProviderFactory,
) {
    fun login(loginRequest: LoginRequest): LoginResponse {
        val user = (userService.findByLoginId(loginRequest.loginId)
            ?: throw IllegalArgumentException("유저가 없어용"))

        runCatching {
            loginWithType(user, loginRequest)
        }.onSuccess {
            logger.info { "$LOG_PREFIX ${user.loginId} ${user.loginType} 인증 성공" }
        }.onFailure {
            logger.info { "$LOG_PREFIX ${user.loginId} ${user.loginType} 인증 실패" }
            throw DoriDoriException.of(DoriDoriExceptionType.LOGIN_FAILED)
        }

        return LoginResponse(
            accessToken = jwtService.createAccessToken(user.id!!.toHexString())
        )
    }

    private fun loginWithType(user: User, loginRequest: LoginRequest) {
        when(loginRequest.loginType) {
            LoginType.BASIC -> passwordService.matchPassword(loginRequest.loginPassword!!, user.password!!)
            LoginType.APPLE -> authProviderFactory.findBy(LoginType.APPLE).socialLogin(user)
            LoginType.KAKAO -> authProviderFactory.findBy(LoginType.KAKAO).socialLogin(user)
            else -> DoriDoriException.of(DoriDoriExceptionType.COMMON_ERROR)
        }
    }

    fun sendCertMail(mailAuthRequest: MailAuthRequest) {
        val certificationNumber = createCertificationNumber()
        certMailService.create(mailAuthRequest.email, certificationNumber)
        gmailSender.send(
            email = mailAuthRequest.email,
            title = "도리도리 인증번호가 발급되었습니다.",
            text = MailTemplate.createCertTemplate(certificationNumber)
        )
    }

    private fun createCertificationNumber(): String {
        return RandomStringUtils.randomNumeric(6)
    }

    companion object {
        const val LOG_PREFIX = "[인증 서비스]"
    }
}