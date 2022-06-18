package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.UserDto
import kr.mashup.bangwidae.asked.service.UserService
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
@Api(tags = ["유저 컨트롤러"])
class UserController(
	private val userService: UserService
) {
	@ApiOperation("유저 찾기 메소드")
	@GetMapping("/{id}")
	fun findUser(@PathVariable id: ObjectId): UserDto {
		return UserDto.from(userService.findById(id))
	}
}