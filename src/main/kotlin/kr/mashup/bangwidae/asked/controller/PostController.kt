package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.ApiResponse
import kr.mashup.bangwidae.asked.controller.dto.CursorResult
import kr.mashup.bangwidae.asked.controller.dto.PostDto
import kr.mashup.bangwidae.asked.controller.dto.PostWriteRequest
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.service.PostService
import org.bson.types.ObjectId
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@Api(tags = ["포스트 컨트롤러"])
@RestController
@RequestMapping("/api/v1/posts")
class PostController(
    private val postService: PostService
) {
    @ApiOperation("포스트 글 작성")
    @PostMapping
    fun writePost(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @RequestBody postWriteRequest: PostWriteRequest
    ): ApiResponse<PostDto> {
        val savedPost = postService.upsert(postWriteRequest.toEntity(user.id!!))
        return ApiResponse.success(PostDto.from(savedPost))
    }

    @ApiOperation("거리 반경 포스트 글 페이징")
    @GetMapping("/near")
    fun getNearPosts(
        @RequestParam longitude: Double,
        @RequestParam latitude: Double,
        @RequestParam meterDistance: Double,
        @RequestParam size: Int,
        @RequestParam lastId: ObjectId?
    ): ApiResponse<CursorResult<PostDto>> {
        return ApiResponse.success(postService.getNearPost(longitude, latitude, meterDistance, lastId, size))
    }
}