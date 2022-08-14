package kr.mashup.bangwidae.asked.service.notification

import kr.mashup.bangwidae.asked.model.document.Notification
import org.bson.types.ObjectId

sealed interface NotificationSpec

interface NotificationGenerator {
    fun support(spec: NotificationSpec): Boolean
    fun generate(spec: NotificationSpec): List<Notification>
}

data class QuestionAnsweredNotificationSpec(
    val answerId: ObjectId,
) : NotificationSpec

data class QuestionReceivedNotificationSpec(
    val questionId: ObjectId,
) : NotificationSpec

data class PostCommentedNotificationSpec(
    val commentId: ObjectId,
) : NotificationSpec

data class LevelUpNotificationSpec(
    val userId: ObjectId,
) : NotificationSpec
