package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.ApiResponse
import kr.mashup.bangwidae.asked.controller.dto.JoinUserRequest
import kr.mashup.bangwidae.asked.controller.dto.JoinUserResponse
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.service.UserService
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
	): ApiResponse<JoinUserResponse> {
		return ApiResponse.success(userService.joinUser(joinUserRequest))
	}

	// 우선 principal 동작 테스트 용도
	@ApiOperation("내 정보")
	@GetMapping("/me")
	fun getMyInfo(
		@ApiIgnore @AuthenticationPrincipal user: User
	): ApiResponse<String> {
		return ApiResponse.success(user.nickname)
	}
}