package kr.mashup.bangwidae.asked.service.event

import org.bson.types.ObjectId

data class QuestionWriteEvent(
    val questionId: ObjectId,
) : NotificationEvent

data class AnswerWriteEvent(
    val answerId: ObjectId,
) : NotificationEvent

data class CommentWriteEvent(
    val commentId: ObjectId,
) : NotificationEvent

data class LevelUpEvent(
    val userId: ObjectId,
) : NotificationEvent
