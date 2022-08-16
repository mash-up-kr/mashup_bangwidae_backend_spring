package kr.mashup.bangwidae.asked.service.notification

import kr.mashup.bangwidae.asked.model.document.Notification
import kr.mashup.bangwidae.asked.repository.NotificationRepository
import kr.mashup.bangwidae.asked.service.event.NotificationEvent
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val notificationGenerators: List<NotificationGenerator>,
) {
    fun generate(event: NotificationEvent): List<Notification> {
        val notifications = notificationGenerators
            .first { it.support(event) }
            .generate(event)

        return notificationRepository.saveAll(notifications)
    }
}
