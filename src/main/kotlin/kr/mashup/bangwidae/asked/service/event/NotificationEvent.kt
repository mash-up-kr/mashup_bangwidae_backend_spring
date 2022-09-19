package kr.mashup.bangwidae.asked.service.event

import kr.mashup.bangwidae.asked.service.notification.NotificationService
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

sealed interface NotificationEvent

@Component
class NotificationEventHandler(
    private val notificationService: NotificationService,
) {
    @Async
    @EventListener
    fun notificationEventListener(event: NotificationEvent) {
        notificationService.generate(event)
    }
}
