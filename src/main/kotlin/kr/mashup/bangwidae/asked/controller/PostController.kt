package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.ApiResponse
import kr.mashup.bangwidae.asked.controller.dto.PostDto
import kr.mashup.bangwidae.asked.controller.dto.PostRequest
import kr.mashup.bangwidae.asked.model.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore

@Api(tags = ["포스트 컨트롤러"])
@RestController
@RequestMapping("/api/v1/post")
class PostController {
	@ApiOperation("포스트 글 작성")
	@PostMapping("/post")
	fun writePost(
		@ApiIgnore @AuthenticationPrincipal user: User,
		@RequestBody postRequest: PostRequest
	): ApiResponse<PostDto> {
		return ApiResponse.success(true)
	}
}