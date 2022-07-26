package kr.mashup.bangwidae.asked.controller.dto

import io.swagger.annotations.ApiModelProperty
import org.bson.types.ObjectId

data class QuestionWriteRequest(
    @ApiModelProperty(value = "질문 내용", example = "질문 내용 string")
    val content: String,
    @ApiModelProperty(value = "질문 대상자 user id", example = "62b49a12507aeb02e6534572")
    val toUserId: ObjectId,
    val longitude: Double,
    val latitude: Double,
)

data class QuestionEditRequest(
    @ApiModelProperty(value = "질문 내용", example = "질문 내용 string")
    val content: String,
)

data class AnswerWriteRequest(
    @ApiModelProperty(value = "답변 내용", example = "질문 내용 string")
    val content: String,
    val longitude: Double,
    val latitude: Double,
)

data class AnswerEditRequest(
    @ApiModelProperty(value = "질문 내용", example = "질문 내용 string")
    val content: String,
)
