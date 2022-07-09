package kr.mashup.bangwidae.asked.service.auth

import kr.mashup.bangwidae.asked.config.auth.jwt.JwtService
import kr.mashup.bangwidae.asked.config.auth.password.PasswordService
import kr.mashup.bangwidae.asked.controller.dto.*
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.external.mail.GmailSender
import kr.mashup.bangwidae.asked.model.LoginType
import kr.mashup.bangwidae.asked.model.MailTemplate
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.repository.UserRepository
import kr.mashup.bangwidae.asked.service.CertMailService
import kr.mashup.bangwidae.asked.service.UserService
import mu.KotlinLogging
import org.apache.commons.lang3.RandomStringUtils
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class AuthService(
    private val gmailSender: GmailSender,

    private val jwtService: JwtService,
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val certMailService: CertMailService,
    private val passwordService: PasswordService,
    private val authProviderFactory: AuthProviderFactory,
) {
    fun login(loginRequest: LoginRequest): LoginResponse {
        val user = userService.findByEmail(loginRequest.email)
            ?: throw DoriDoriException.of(DoriDoriExceptionType.LOGIN_FAILED)


        val refreshToken = jwtService.createRefreshToken(user.id!!.toHexString())
        runCatching {
            loginWithType(user, loginRequest)
        }.onSuccess {
            logger.info { "$LOG_PREFIX ${user.email} ${user.loginType} 인증 성공" }
            userRepository.save(
                user.updateRefreshToken(refreshToken)
            )
        }.onFailure {
            logger.info { "$LOG_PREFIX ${user.email} ${user.loginType} 인증 실패" }
            throw DoriDoriException.of(DoriDoriExceptionType.LOGIN_FAILED)
        }

        return LoginResponse(
            accessToken = jwtService.createAccessToken(user.id.toHexString()),
            refreshToken = refreshToken
        )
    }

    private fun loginWithType(user: User, loginRequest: LoginRequest) {
        when(loginRequest.loginType) {
            LoginType.BASIC -> passwordService.matchPassword(loginRequest.password!!, user.password!!)
            LoginType.APPLE -> authProviderFactory.findBy(LoginType.APPLE).socialLogin(user)
            LoginType.KAKAO -> authProviderFactory.findBy(LoginType.KAKAO).socialLogin(user)
            else -> DoriDoriException.of(DoriDoriExceptionType.COMMON_ERROR)
        }
    }

    fun sendCertMail(certMailSendRequest: CertMailSendRequest) {
        userService.checkDuplicatedUserByEmail(certMailSendRequest.email)

        val certificationNumber = createCertificationNumber()
        certMailService.create(certMailSendRequest.email, certificationNumber)
        gmailSender.send(
            email = certMailSendRequest.email,
            title = "도리도리 인증번호가 발급되었습니다.",
            text = MailTemplate.createCertTemplate(certificationNumber)
        )
    }

    private fun createCertificationNumber(): String {
        return RandomStringUtils.randomNumeric(6)
    }

    fun certMail(certMailRequest: CertMailRequest) {
        certMailService.certificate(certMailRequest.email, certMailRequest.certificationNumber)
    }

    fun issueToken(refreshToken: String): IssueTokenResponse {
        logger.info { "토큰 재발급 요청" }
        return try {
            val userId = jwtService.decodeToken(refreshToken)
            val user = userService.findById(ObjectId(userId))
            if (user.refreshToken == refreshToken) {
                logger.info { "토큰 발급 성공" }
                val accessToken = jwtService.createAccessToken(userId!!)
                IssueTokenResponse(accessToken)
            } else {
                logger.info { "요청 파라미터가 DB에 저장된 리프레시 토큰과 다름" }
                IssueTokenResponse()
            }
        } catch (e: Exception) {
            logger.info { "토큰 발급 실패 ${e.message}" }
            IssueTokenResponse()
        }
    }

    companion object {
        const val LOG_PREFIX = "[인증 서비스]"
    }
}