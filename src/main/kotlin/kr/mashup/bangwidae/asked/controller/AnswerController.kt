package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.AnswerEditRequest
import kr.mashup.bangwidae.asked.controller.dto.ApiResponse
import kr.mashup.bangwidae.asked.controller.path.ApiPath
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.service.question.AnswerService
import org.bson.types.ObjectId
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@Api(tags = ["답변 컨트롤러"])
@RestController
@RequestMapping(ApiPath.ROOT)
class AnswerController(
    private val answerService: AnswerService,
) {
    @ApiOperation("답변 수정")
    @PatchMapping(ApiPath.ANSWER_EDIT)
    fun editAnswerContent(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @PathVariable answerId: ObjectId,
        @RequestBody answerEditRequest: AnswerEditRequest,
    ): ApiResponse<ObjectId> {
        return answerService.edit(
            answerId = answerId,
            request = answerEditRequest,
            user = user,
        ).let {
            ApiResponse.success(it.id!!)
        }
    }

    @ApiOperation("답변 삭제")
    @DeleteMapping(ApiPath.ANSWER_DELETE)
    fun deleteAnswer(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @PathVariable answerId: ObjectId,
    ): ApiResponse<ObjectId> {
        return answerService.delete(
            answerId = answerId,
            user = user,
        ).let {
            ApiResponse.success(it.id!!)
        }
    }

    @ApiOperation("답변 좋아요")
    @PostMapping(ApiPath.ANSWER_LIKE)
    fun likeAnswer(
        @ApiIgnore @AuthenticationPrincipal user: User, @PathVariable answerId: ObjectId
    ): ApiResponse<Boolean> {
        answerService.answerLike(answerId, user.id!!)
        return ApiResponse.success(true)
    }

    @ApiOperation("답변 좋아요 취소")
    @DeleteMapping(ApiPath.ANSWER_CANCEL_LIKE)
    fun unlikeComment(
        @ApiIgnore @AuthenticationPrincipal user: User, @PathVariable answerId: ObjectId
    ): ApiResponse<Boolean> {
        answerService.answerUnlike(answerId, user.id!!)
        return ApiResponse.success(true)
    }
}
