package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.ApiResponse
import kr.mashup.bangwidae.asked.controller.dto.CommentEditRequest
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.service.post.CommentService
import org.bson.types.ObjectId
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@Api(tags = ["댓글 컨트롤러"])
@RestController
@RequestMapping("/api/v1/comments")
class CommentController(
    private val commentService: CommentService
) {
    @ApiOperation("댓글 수정")
    @PatchMapping("/{commentId}")
    fun editCommentContent(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @PathVariable commentId: ObjectId,
        @RequestBody commentEditRequest: CommentEditRequest,
    ): ApiResponse<ObjectId> {
        return commentService.edit(
            commentId = commentId,
            request = commentEditRequest,
            user = user,
        ).let {
            ApiResponse.success(it.id!!)
        }
    }

    @ApiOperation("댓글 삭제")
    @DeleteMapping("/{commentId}")
    fun deleteAnswer(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @PathVariable commentId: ObjectId,
    ): ApiResponse<ObjectId> {
        return commentService.delete(
            commentId = commentId,
            user = user,
        ).let {
            ApiResponse.success(it.id!!)
        }
    }
}