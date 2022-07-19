package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.ApiResponse
import kr.mashup.bangwidae.asked.controller.dto.CommentDto
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
    ): ApiResponse<CommentDto> {
        val comment = commentService.findById(commentId)
        return commentService.edit(
            user = user,
            comment = comment.update(commentEditRequest)
        ).let {
            ApiResponse.success(CommentDto.from(user, it))
        }
    }

    @ApiOperation("댓글 삭제")
    @DeleteMapping("/{commentId}")
    fun deleteAnswer(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @PathVariable commentId: ObjectId,
    ): ApiResponse<CommentDto> {
        val comment = commentService.findById(commentId)
        return commentService.delete(
            comment = comment,
            user = user,
        ).let {
            ApiResponse.success(CommentDto.from(user, it))
        }
    }
}