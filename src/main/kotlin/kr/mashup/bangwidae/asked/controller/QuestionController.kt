package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.*
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.service.question.AnswerService
import kr.mashup.bangwidae.asked.service.question.QuestionService
import org.bson.types.ObjectId
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@Api(tags = ["질문 컨트롤러"])
@RestController
@RequestMapping("/api/v1/questions")
class QuestionController(
    private val questionService: QuestionService,
    private val answerService: AnswerService,
) {
    @ApiOperation("질문/답변 단건 조회")
    @GetMapping("/{questionId}")
    fun getDetailById(
        @ApiIgnore @AuthenticationPrincipal user: User?,
        @PathVariable questionId: ObjectId,
    ): ApiResponse<QuestionDetailDto> {
        return questionService.findDetailById(
            user = user,
            questionId = questionId,
        ).let {
            ApiResponse.success(
                QuestionDetailDto.from(it)
            )
        }
    }

    @ApiOperation("답변완료(본인 외 사용자)")
    @GetMapping("/answered")
    fun getMyAnsweredQuestions(
        @RequestParam userId: ObjectId,
        @RequestParam size: Int,
        @RequestParam(required = false) lastId: ObjectId?,
    ): ApiResponse<AnsweredQuestionsDto> {
        return questionService.findAnswerCompleteByToUser(
            userId = userId,
            lastId = lastId,
            size = size + 1,
        ).let {
            ApiResponse.success(
                AnsweredQuestionsDto.from(
                    questions = it,
                    requestedSize = size,
                )
            )
        }
    }

    @ApiOperation("질문 작성")
    @PostMapping
    fun writeQuestion(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @RequestBody questionWriteRequest: QuestionWriteRequest,
    ): ApiResponse<ObjectId> {
        return questionService.write(
            request = questionWriteRequest,
            user = user,
        ).let {
            ApiResponse.success(it.id!!)
        }
    }

    @ApiOperation("질문 수정")
    @PatchMapping("/{questionId}")
    fun editQuestionContent(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @PathVariable questionId: ObjectId,
        @RequestBody questionEditRequest: QuestionEditRequest,
    ): ApiResponse<ObjectId> {
        return questionService.edit(
            questionId = questionId,
            request = questionEditRequest,
            user = user,
        ).let {
            ApiResponse.success(it.id!!)
        }
    }

    @ApiOperation("질문 삭제")
    @DeleteMapping("/{questionId}")
    fun deleteQuestion(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @PathVariable questionId: ObjectId,
    ): ApiResponse<ObjectId> {
        return questionService.delete(
            questionId = questionId,
            user = user,
        ).let {
            ApiResponse.success(it.id!!)
        }
    }

    @ApiOperation("질문 거절")
    @PostMapping("/{questionId}/deny")
    fun denyQuestion(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @PathVariable questionId: ObjectId,
    ): ApiResponse<ObjectId> {
        return questionService.deny(
            questionId = questionId,
            user = user,
        ).let {
            ApiResponse.success(it.id!!)
        }
    }

    @ApiOperation("질문 답변")
    @PostMapping("/{questionId}/answer")
    fun answerQuestion(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @PathVariable questionId: ObjectId,
        @RequestBody answerWriteRequest: AnswerWriteRequest,
    ): ApiResponse<ObjectId> {
        return answerService.write(
            questionId = questionId,
            request = answerWriteRequest,
            user = user,
        ).let {
            ApiResponse.success(it.id!!)
        }
    }
}
