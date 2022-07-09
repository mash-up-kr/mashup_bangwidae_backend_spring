package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.*
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
        @ApiIgnore @AuthenticationPrincipal user: User, @RequestBody postWriteRequest: PostWriteRequest
    ): ApiResponse<PostDto> {
        val savedPost = postService.save(postWriteRequest.toEntity(user.id!!))
        return ApiResponse.success(PostDto.from(user, savedPost))
    }

    @ApiOperation("포스트 글 수정")
    @PatchMapping("/{id}")
    fun editPost(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @RequestBody postEditRequest: PostEditRequest,
        @PathVariable id: ObjectId
    ): ApiResponse<PostDto> {
        val post = postService.findById(id)
        val updatedPost = postService.update(user, post.update(postEditRequest))
        return ApiResponse.success(PostDto.from(user, updatedPost))
    }

    @ApiOperation("포스트 글 삭제")
    @DeleteMapping("/{id}")
    fun deletePost(
        @ApiIgnore @AuthenticationPrincipal user: User, @PathVariable id: ObjectId
    ): ApiResponse<Boolean> {
        val post = postService.findById(id)
        postService.delete(user, post)
        return ApiResponse.success(true)
    }

    @ApiOperation("거리 반경 포스트 글 페이징")
    @GetMapping("/near")
    fun getNearPosts(
        @RequestParam longitude: Double,
        @RequestParam latitude: Double,
        @RequestParam meterDistance: Double,
        @RequestParam size: Int,
        @RequestParam(required = false) lastId: ObjectId?
    ): ApiResponse<CursorResult<PostDto>> {
        return ApiResponse.success(postService.getNearPost(longitude, latitude, meterDistance, lastId, size))
    }

    @ApiOperation("포스트 글 조회")
    @GetMapping("/{id}")
    fun getPostById(
        @PathVariable id: ObjectId
    ): ApiResponse<PostDto> {
        return ApiResponse.success(postService.getPostById(id))
    }
}