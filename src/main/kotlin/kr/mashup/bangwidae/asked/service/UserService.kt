package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.config.auth.jwt.JwtService
import kr.mashup.bangwidae.asked.controller.dto.JoinUserRequest
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
	private val userRepository: UserRepository
) {

	fun jointUser(joinUserRequest: JoinUserRequest): String {
		val user = userRepository.save(
			User.createBasicUser(
				loginId = joinUserRequest.loginId,
				password = joinUserRequest.password
			)
		)
		return jwtService.createAccessToken(user.id!!.toHexString())
	}

	@Transactional(readOnly = true)
	fun findById(id: ObjectId): User {
		return userRepository.findByIdOrNull(id) ?: throw RuntimeException("User {id: $id} Not Found")
	}
}