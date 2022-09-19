package kr.mashup.bangwidae.asked.service.notification

import kr.mashup.bangwidae.asked.service.event.NotificationEvent
import kr.mashup.bangwidae.asked.model.document.Notification

sealed interface NotificationSpec

interface NotificationGenerator {
    fun support(event: NotificationEvent): Boolean
    fun generate(event: NotificationEvent): List<Notification>
}
