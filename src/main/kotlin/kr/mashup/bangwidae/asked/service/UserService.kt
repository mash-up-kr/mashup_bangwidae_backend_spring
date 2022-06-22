package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.config.auth.jwt.JwtService
import kr.mashup.bangwidae.asked.config.auth.password.PasswordService
import kr.mashup.bangwidae.asked.controller.dto.JoinUserRequest
import kr.mashup.bangwidae.asked.controller.dto.JoinUserResponse
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class UserService(
	private val jwtService: JwtService,
	private val userRepository: UserRepository,
	private val passwordService: PasswordService
) {

	fun jointUser(joinUserRequest: JoinUserRequest): JoinUserResponse {
		passwordService.validatePassword(joinUserRequest.password)

		val encodedPassword = passwordService.encodePassword(joinUserRequest.password)
		val user = userRepository.save(
			User.createBasicUser(
				loginId = joinUserRequest.loginId,
				password = encodedPassword
			)
		)
		return JoinUserResponse(jwtService.createAccessToken(user.id!!.toHexString()))
	}

	@Transactional(readOnly = true)
	fun findById(id: ObjectId): User {
		return userRepository.findByIdOrNull(id) ?: throw RuntimeException("User {id: $id} Not Found")
	}

	@Transactional(readOnly = true)
	fun findByLoginId(loginId: String): User? {
		return userRepository.findByLoginId(loginId)
	}
}