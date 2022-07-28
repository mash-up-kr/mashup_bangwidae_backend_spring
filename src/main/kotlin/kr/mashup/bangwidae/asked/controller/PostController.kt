package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.*
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.service.post.CommentService
import kr.mashup.bangwidae.asked.service.post.PostLikeService
import kr.mashup.bangwidae.asked.service.post.PostService
import org.bson.types.ObjectId
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@Api(tags = ["포스트 컨트롤러"])
@RestController
@RequestMapping("/api/v1/posts")
class PostController(
    private val postService: PostService,
    private val postLikeService: PostLikeService,
    private val commentService: CommentService
) {
    @ApiOperation("포스트 글 작성")
    @PostMapping
    fun writePost(
        @ApiIgnore @AuthenticationPrincipal user: User, @RequestBody postWriteRequest: PostWriteRequest
    ): ApiResponse<PostResultDto> {
        val post = postWriteRequest.toEntity(user.id!!)
        return postService.write(post)
            .let {
                ApiResponse.success(PostResultDto.from(user, it))
            }
    }

    @ApiOperation("포스트 글 수정")
    @PatchMapping("/{id}")
    fun editPost(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @RequestBody postEditRequest: PostEditRequest,
        @PathVariable id: ObjectId
    ): ApiResponse<PostResultDto> {
        return postService.edit(
            postId = id,
            user = user,
            request = postEditRequest
        ).let {
            ApiResponse.success(PostResultDto.from(user, it))
        }
    }

    @ApiOperation("포스트 글 삭제")
    @DeleteMapping("/{id}")
    fun deletePost(
        @ApiIgnore @AuthenticationPrincipal user: User, @PathVariable id: ObjectId
    ): ApiResponse<Boolean> {
        postService.delete(postId = id, user = user)
        return ApiResponse.success(true)
    }

    @ApiOperation("포스트 글 좋아요")
    @PostMapping("/{id}/like")
    fun likePost(
        @ApiIgnore @AuthenticationPrincipal user: User, @PathVariable id: ObjectId
    ): ApiResponse<Boolean> {
        postLikeService.postLike(id, user.id!!)
        return ApiResponse.success(true)
    }

    @ApiOperation("포스트 글 좋아요 취소")
    @DeleteMapping("/{id}/like")
    fun unlikePost(
        @ApiIgnore @AuthenticationPrincipal user: User, @PathVariable id: ObjectId
    ): ApiResponse<Boolean> {
        postLikeService.postUnlike(id, user.id!!)
        return ApiResponse.success(true)
    }

    @ApiOperation("거리 반경 포스트 글 페이징")
    @GetMapping("/near")
    fun getNearPosts(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @RequestParam longitude: Double,
        @RequestParam latitude: Double,
        @RequestParam meterDistance: Double,
        @RequestParam size: Int,
        @RequestParam(required = false) lastId: ObjectId?
    ): ApiResponse<CursorResult<PostDto>> {
        return postService.getNearPost(user.id!!, longitude, latitude, meterDistance, lastId, size + 1)
            .let {
                ApiResponse.success(CursorResult.from(values = it, requestedSize = size))
            }
    }

    @ApiOperation("포스트 글 조회")
    @GetMapping("/{id}")
    fun getPostById(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @PathVariable id: ObjectId
    ): ApiResponse<PostDto> {
        return postService.getPostById(user.id!!, id)
            .let {
                ApiResponse.success(it)
            }
    }

    @ApiOperation("댓글 작성")
    @PostMapping("/{postId}/comment")
    fun commentPost(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @PathVariable postId: ObjectId,
        @RequestBody commentWriteRequest: CommentWriteRequest,
    ): ApiResponse<CommentResultDto> {
        return commentService.write(
            user = user,
            postId = postId,
            comment = commentWriteRequest.toEntity(user.id!!, postId)
        ).let {
            ApiResponse.success(CommentResultDto.from(user, it))
        }
    }

    @ApiOperation("댓글 조희 페이징")
    @GetMapping("/{postId}/comment")
    fun getComments(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @PathVariable postId: ObjectId,
        @RequestParam size: Int,
        @RequestParam(required = false) lastId: ObjectId?
    ): ApiResponse<CursorResult<CommentDto>> {
        return commentService.getCommentsByPostId(user.id!!, postId, lastId, size + 1)
            .let {
                ApiResponse.success(CursorResult.from(values = it, requestedSize = size))
            }
    }
}