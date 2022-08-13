package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.config.auth.jwt.JwtService
import kr.mashup.bangwidae.asked.config.auth.password.PasswordService
import kr.mashup.bangwidae.asked.controller.dto.EditUserSettingsRequest
import kr.mashup.bangwidae.asked.controller.dto.JoinUserRequest
import kr.mashup.bangwidae.asked.controller.dto.JoinUserResponse
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.external.aws.S3ImageUploader
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.UserSettings
import kr.mashup.bangwidae.asked.repository.UserRepository
import kr.mashup.bangwidae.asked.service.question.QuestionService
import org.bson.types.ObjectId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val certMailService: CertMailService,
    private val passwordService: PasswordService,
    private val questionService: QuestionService,
    private val s3ImageUploader: S3ImageUploader,
    private val wardService: WardService
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
            -> "답변을 기다리는 질문이 ${questionService.countAnswerWaitingByToUser(user)}개 있어요"
            // TODO 미확인 질문 카운트
            HeaderTextType.UNREAD_QUESTION_COUNT
            -> "새로운 질문이 n개 도착했어요"
        }
    }

    fun updateNickname(user: User, nickname: String): Boolean {
        checkDuplicatedUserByNickname(nickname)
        userRepository.save(
            user.updateNickname(nickname)
        )
        return true
    }

    @Transactional
    fun updateProfile(user: User, description: String, tags: List<String>, representativeWardId: ObjectId?): Boolean {
        userRepository.save(user.updateProfile(description, tags))
        wardService.updateRepresentativeWard(user, representativeWardId)
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

    fun editUserSettings(user: User, editUserSettingsRequest: EditUserSettingsRequest): User {
        val newSettings = UserSettings(
            notification = editUserSettingsRequest.notification,
            nightNotification = editUserSettingsRequest.nightNotification,
            locationInfo = editUserSettingsRequest.locationInfo,
        )
        return userRepository.save(user.updateSettings(newSettings))
    }
}

enum class HeaderTextType {
    UNREAD_QUESTION_COUNT,
    ANSWER_WAITING_QUESTION_COUNT,
}
