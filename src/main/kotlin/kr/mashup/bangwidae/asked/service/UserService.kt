package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.config.auth.jwt.JwtService
import kr.mashup.bangwidae.asked.config.auth.password.PasswordService
import kr.mashup.bangwidae.asked.controller.dto.JoinUserRequest
import kr.mashup.bangwidae.asked.controller.dto.JoinUserResponse
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.repository.UserRepository
import kr.mashup.bangwidae.asked.utils.S3ImageUtil
import kr.mashup.bangwidae.asked.utils.UploadDirName
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
    private val s3ImageUtil: S3ImageUtil
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
        return JoinUserResponse(jwtService.createAccessToken(user.id!!.toHexString()))
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
        val uploadedUrl = s3ImageUtil.upload(image, UploadDirName.PROFILE)
        userRepository.save(user.updateProfileImageUrl(uploadedUrl))
        return uploadedUrl
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