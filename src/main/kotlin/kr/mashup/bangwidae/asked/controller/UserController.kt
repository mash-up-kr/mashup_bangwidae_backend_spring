package kr.mashup.bangwidae.asked.controller

import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.service.UserService
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(
	private val userService: UserService
) {
	@GetMapping("/{id}")
	fun findUser(@PathVariable id: ObjectId): User {
		return userService.findById(id)
	}
}