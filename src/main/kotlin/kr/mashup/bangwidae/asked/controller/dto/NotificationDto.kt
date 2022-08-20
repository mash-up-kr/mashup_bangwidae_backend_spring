package kr.mashup.bangwidae.asked.controller.dto

import kr.mashup.bangwidae.asked.model.document.Notification
import kr.mashup.bangwidae.asked.service.notification.NotificationType
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class NotificationDto(
    val id: ObjectId,
    val type: NotificationType,
    val title: String,
    val content: String,
    val read: Boolean,
    val urlScheme: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(notification: Notification): NotificationDto {
            return NotificationDto(
                id = notification.id!!,
                type = notification.type,
                title = notification.title,
                content = notification.content,
                read = notification.read,
                urlScheme = notification.urlScheme,
                createdAt = notification.createdAt!!,
            )
        }
    }
}
