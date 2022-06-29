package kr.mashup.bangwidae.asked.service.auth

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.mashup.bangwidae.asked.config.auth.jwt.JwtService
import kr.mashup.bangwidae.asked.config.auth.password.PasswordService
import kr.mashup.bangwidae.asked.controller.dto.LoginRequest
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.external.mail.GmailSender
import kr.mashup.bangwidae.asked.model.LoginType
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.service.CertMailService
import kr.mashup.bangwidae.asked.service.UserService
import org.bson.types.ObjectId

internal class AuthServiceTest : FunSpec({
    lateinit var gmailSender: GmailSender
    lateinit var jwtService: JwtService
    lateinit var certMailService: CertMailService
    lateinit var passwordService: PasswordService
    lateinit var userService: UserService
    lateinit var authProviderFactory: AuthProviderFactory
    lateinit var authService: AuthService

    beforeTest {
        gmailSender = mockk()
        jwtService = mockk()
        certMailService = mockk()
        passwordService = mockk()
        userService = mockk()
        authProviderFactory = mockk()

        authService = AuthService(
            gmailSender = gmailSender,
            jwtService = jwtService,
            userService = userService,
            certMailService = certMailService,
            passwordService = passwordService,
            authProviderFactory = authProviderFactory,
        )
    }

    context("로그인 시도시") {
        val loginRequest = LoginRequest("email", "password", LoginType.BASIC)
        test("유저가 없다면 LOGIN_FAILED Error 를 던진다.") {
            every { userService.findByEmail(any()) } returns null

            val exception = shouldThrow<DoriDoriException> {
                authService.login(loginRequest)
            }
            exception.code shouldBe DoriDoriExceptionType.LOGIN_FAILED.code
        }
        test("LOGIN_FAILED Error 를 던진다.") {
            every { userService.findByEmail(any()) } returns mockk()

            val exception = shouldThrow<DoriDoriException> {
                authService.login(loginRequest)
            }
            exception.code shouldBe DoriDoriExceptionType.LOGIN_FAILED.code
        }

        test("인증에 성공했다면 jwtToken 을 반환한다.") {
            val expectedToken = "token"
            val basicUser = User(
                id = ObjectId("62b6ec5b092d0d7ae1504ace"),
                nickname = "",
                email = "",
                password = "",
                providerId = null,
                loginType = LoginType.BASIC,
                tags = listOf()
            )

            every { userService.findByEmail(any()) } returns basicUser
            every { passwordService.matchPassword(any(), any()) } returns Unit

            every { jwtService.createAccessToken(any()) } returns expectedToken
            authService.login(loginRequest).accessToken shouldBe expectedToken
        }
    }

})