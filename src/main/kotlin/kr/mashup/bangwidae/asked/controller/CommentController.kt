package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.ApiResponse
import kr.mashup.bangwidae.asked.controller.dto.CommentResultDto
import kr.mashup.bangwidae.asked.controller.dto.CommentEditRequest
import kr.mashup.bangwidae.asked.controller.path.ApiPath
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.service.post.CommentLikeService
import kr.mashup.bangwidae.asked.service.post.CommentService
import org.bson.types.ObjectId
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@Api(tags = ["댓글 컨트롤러"])
@RestController
@RequestMapping(ApiPath.ROOT)
class CommentController(
    private val commentService: CommentService,
    private val commentLikeService: CommentLikeService
) {
    @ApiOperation("댓글 수정")
    @PatchMapping(ApiPath.COMMENT_EDIT)
    fun editCommentContent(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @PathVariable commentId: ObjectId,
        @RequestBody commentEditRequest: CommentEditRequest,
    ): ApiResponse<CommentResultDto> {
        return commentService.edit(
            commentId = commentId,
            user = user,
            request = commentEditRequest
        ).let {
            ApiResponse.success(CommentResultDto.from(user, it))
        }
    }

    @ApiOperation("댓글 삭제")
    @DeleteMapping(ApiPath.COMMENT_DELETE)
    fun deleteAnswer(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @PathVariable commentId: ObjectId,
    ): ApiResponse<CommentResultDto> {
        return commentService.delete(
            commentId = commentId,
            user = user,
        ).let {
            ApiResponse.success(CommentResultDto.from(user, it))
        }
    }

    @ApiOperation("댓글 좋아요")
    @PostMapping(ApiPath.COMMENT_LIKE)
    fun likeComment(
        @ApiIgnore @AuthenticationPrincipal user: User, @PathVariable commentId: ObjectId
    ): ApiResponse<Boolean> {
        commentLikeService.commentLike(commentId, user.id!!)
        return ApiResponse.success(true)
    }

    @ApiOperation("댓글 좋아요 취소")
    @DeleteMapping(ApiPath.COMMENT_CANCEL_LIKE)
    fun unlikeComment(
        @ApiIgnore @AuthenticationPrincipal user: User, @PathVariable commentId: ObjectId
    ): ApiResponse<Boolean> {
        commentLikeService.commentUnlike(commentId, user.id!!)
        return ApiResponse.success(true)
    }
}