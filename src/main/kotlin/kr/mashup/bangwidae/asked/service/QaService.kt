package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.config.auth.jwt.JwtService
import kr.mashup.bangwidae.asked.config.auth.password.PasswordService
import kr.mashup.bangwidae.asked.controller.dto.JoinUserResponse
import kr.mashup.bangwidae.asked.controller.dto.QaJoinUserRequest
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.repository.AnswerRepository
import kr.mashup.bangwidae.asked.repository.PostRepository
import kr.mashup.bangwidae.asked.repository.QuestionRepository
import kr.mashup.bangwidae.asked.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class QaService(
    private val passwordService: PasswordService,
    private val termsService: TermsService,
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val postRepository: PostRepository,
    private val answerRepository: AnswerRepository,
    private val questionRepository: QuestionRepository,
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
            ).copy(
                nickname = joinUserRequest.nickname ?: "test nickname",
                description = joinUserRequest.description ?: "test description",
                tags = joinUserRequest.tags ?: listOf("testTag1", "testTag2")
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
            userId = user.id.toHexString()
        )
    }

    fun findType(id: ObjectId): String{
        return if (postRepository.existsById(id)) "POST"
        else if(questionRepository.existsById(id)) "QUESTION"
        else if(answerRepository.existsById(id)) "QUESTION"
        else throw DoriDoriException.of(DoriDoriExceptionType.UNKNOWN_TYPE)
    }
}