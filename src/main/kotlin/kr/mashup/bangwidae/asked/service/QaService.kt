package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.config.auth.jwt.JwtService
import kr.mashup.bangwidae.asked.config.auth.password.PasswordService
import kr.mashup.bangwidae.asked.controller.dto.JoinUserResponse
import kr.mashup.bangwidae.asked.controller.dto.QaJoinUserRequest
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class QaService(
    private val passwordService: PasswordService,
    private val termsService: TermsService,
    private val userRepository: UserRepository,
    private val jwtService: JwtService
) {
    fun joinUser(joinUserRequest: QaJoinUserRequest): JoinUserResponse {
        if (userRepository.findByEmail(joinUserRequest.email) != null) {
            throw DoriDoriException.of(DoriDoriExceptionType.DUPLICATED_USER)
        }
        val encodedPassword = passwordService.encodePassword(joinUserRequest.password)
        val user = userRepository.save(
            User.createBasicUser(
                email = joinUserRequest.email,
                password = encodedPassword
            )
        )

        termsService.agreeTerms(user, termsService.getTerms().mapNotNull { it.id })

        val refreshToken = jwtService.createRefreshToken(user.id!!.toHexString())
        userRepository.save(
            user.updateRefreshToken(refreshToken)
        )
        return JoinUserResponse(
            accessToken = jwtService.createAccessToken(user.id.toHexString()),
            refreshToken = refreshToken,
        )
    }
}