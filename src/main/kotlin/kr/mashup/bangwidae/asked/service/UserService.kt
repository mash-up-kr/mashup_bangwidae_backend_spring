package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.config.auth.jwt.JwtService
import kr.mashup.bangwidae.asked.config.auth.password.PasswordService
import kr.mashup.bangwidae.asked.controller.dto.JoinUserRequest
import kr.mashup.bangwidae.asked.controller.dto.JoinUserResponse
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.external.aws.S3ImageUploader
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.repository.UserRepository
import kr.mashup.bangwidae.asked.service.question.QuestionService
import org.bson.types.ObjectId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class UserService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val certMailService: CertMailService,
    private val passwordService: PasswordService,
    private val questionService: QuestionService,
    private val s3ImageUploader: S3ImageUploader
) {

    fun joinUser(joinUserRequest: JoinUserRequest): JoinUserResponse {
        val cert = certMailService.findByEmail(joinUserRequest.email)
        if (!cert.isCertificated) {
            throw DoriDoriException.of(DoriDoriExceptionType.NOT_CERTIFICATED_EMAIL)
        }

        passwordService.validatePassword(joinUserRequest.password)

        val encodedPassword = passwordService.encodePassword(joinUserRequest.password)
        val user = userRepository.save(
            User.createBasicUser(
                email = joinUserRequest.email,
                password = encodedPassword
            )
        )

        val refreshToken = jwtService.createRefreshToken(user.id!!.toHexString())
        userRepository.save(
            user.updateRefreshToken(refreshToken)
        )
        return JoinUserResponse(
            accessToken = jwtService.createAccessToken(user.id.toHexString()),
            refreshToken = refreshToken,
        )
    }

    fun getUserInfo(userId: ObjectId): User {
        return userRepository.findById(userId)
            .orElseThrow { DoriDoriException.of(DoriDoriExceptionType.USER_NOT_FOUND) }
    }

    fun getUserHeaderText(user: User, type: HeaderTextType): String {
        return when (type) {
            HeaderTextType.ANSWER_WAITING_QUESTION_COUNT
            -> "????????? ???????????? ????????? ${questionService.countAnswerWaitingByToUser(user)}??? ?????????"
            // TODO ????????? ?????? ?????????
            HeaderTextType.UNREAD_QUESTION_COUNT
            -> "????????? ????????? n??? ???????????????"
        }
    }

    fun updateNickname(user: User, nickname: String): Boolean {
        checkDuplicatedUserByNickname(nickname)
        userRepository.save(
            user.updateNickname(nickname)
        )
        return true
    }

    fun updateProfile(user: User, description: String, tags: List<String>): Boolean {
        userRepository.save(
            user.updateProfile(description, tags)
        )
        return true
    }

    fun updateProfileImage(user: User, image: MultipartFile): String {
        val uploadedUrl = s3ImageUploader.upload(image, "profile")
        userRepository.save(user.updateProfileImageUrl(uploadedUrl))
        return uploadedUrl
    }

    fun updateToDefaultProfileImage(user: User): Boolean {
        userRepository.save(user.updateProfileImageUrl(User.DEFAULT_PROFILE_IMAGE_URL))
        return true
    }

    fun findById(id: ObjectId): User {
        return userRepository.findByIdOrNull(id) ?: throw RuntimeException("User {id: $id} Not Found")
    }

    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    fun checkDuplicatedUserByEmail(email: String) {
        val user = userRepository.findByEmail(email)
        if (user != null) {
            throw DoriDoriException.of(DoriDoriExceptionType.DUPLICATED_USER)
        }
    }

    fun checkDuplicatedUserByNickname(nickname: String) {
        val user = userRepository.findByNickname(nickname)
        if (user != null) {
            throw DoriDoriException.of(DoriDoriExceptionType.DUPLICATED_NICKNAME)
        }
    }
}

enum class HeaderTextType {
    UNREAD_QUESTION_COUNT,
    ANSWER_WAITING_QUESTION_COUNT,
}
