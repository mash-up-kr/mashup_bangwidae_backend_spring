package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
	private val userRepository: UserRepository
) {
	fun findAllByName(name: String): List<User> {
		return userRepository.findAllByName(name)
	}

	fun findById(id: ObjectId): User {
		return userRepository.findByIdOrNull(id) ?: throw RuntimeException("User {id: $id} Not Found")
	}
}