package kr.mashup.bangwidae.asked.service.notification

import kr.mashup.bangwidae.asked.model.Notification
import kr.mashup.bangwidae.asked.repository.NotificationRepository
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val notificationGenerators: List<NotificationGenerator>,
) {
    fun generate(spec: NotificationSpec): List<Notification> {
        val notification = notificationGenerators
            .first { it.support(spec) }
            .generate(spec)

        return notificationRepository.saveAll(notification)
    }
}
