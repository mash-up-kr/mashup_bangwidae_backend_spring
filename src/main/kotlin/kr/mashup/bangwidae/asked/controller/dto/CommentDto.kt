package kr.mashup.bangwidae.asked.controller.dto

import io.swagger.annotations.ApiModelProperty

data class CommentWriteRequest(
    @ApiModelProperty(value = "댓글 내용", example = "댓글 내용 string")
    val content: String,
)

data class CommentEditRequest(
    @ApiModelProperty(value = "댓글 내용", example = "댓글 내용 string")
    val content: String,
)