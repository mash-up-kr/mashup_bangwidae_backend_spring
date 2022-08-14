package kr.mashup.bangwidae.asked.service.event

import kr.mashup.bangwidae.asked.service.notification.NotificationService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

sealed interface NotificationEvent

@Component
class NotificationEventHandler(
    private val notificationService: NotificationService,
) {
    @Async
    @TransactionalEventListener
    fun notificationEventListener(event: NotificationEvent) {
        notificationService.generate(event)
    }
}
