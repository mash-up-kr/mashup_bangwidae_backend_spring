package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.JoinUserRequest
import kr.mashup.bangwidae.asked.controller.dto.JoinUserResponse
import kr.mashup.bangwidae.asked.controller.dto.UserDto
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.service.UserService
import org.bson.types.ObjectId
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@Api(tags = ["유저 컨트롤러"])
@RestController
@RequestMapping("/api/v1/user")
class UserController(
	private val userService: UserService
) {

	@ApiOperation("ID/PW 회원가입")
	@PostMapping("/join")
	fun joinUser(
		@RequestBody joinUserRequest: JoinUserRequest
	): JoinUserResponse {
		return JoinUserResponse(userService.jointUser(joinUserRequest))
	}

	@ApiOperation("유저 찾기 메소드")
	@GetMapping("/{id}")
	fun findUser(@PathVariable id: ObjectId): UserDto {
		return UserDto.from(userService.findById(id))
	}

	// 우선 principal 동작 테스트 용도
	@ApiOperation("내 정보")
	@GetMapping("/me")
	fun getMyInfo(
		@ApiIgnore @AuthenticationPrincipal user: User
	): String {
		return user.nickname
	}
}