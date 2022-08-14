package kr.mashup.bangwidae.asked.service.notification

import kr.mashup.bangwidae.asked.model.Notification
import kr.mashup.bangwidae.asked.service.event.NotificationEvent

interface NotificationGenerator {
    fun support(event: NotificationEvent): Boolean
    fun generate(event: NotificationEvent): List<Notification>
}
