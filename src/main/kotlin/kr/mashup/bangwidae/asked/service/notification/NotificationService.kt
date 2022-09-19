package kr.mashup.bangwidae.asked.service.notification

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.document.Notification
import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.repository.NotificationRepository
import kr.mashup.bangwidae.asked.service.event.NotificationEvent
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
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

    fun findByUser(user: User, lastId: ObjectId?, size: Int): List<Notification> {
        return notificationRepository.findByUserIdAndIdBeforeOrderByCreatedAtDesc(
            userId = user.id!!,
            lastId = lastId ?: ObjectId(),
            pageRequest = PageRequest.of(0, size),
        )
    }

    fun countUnreadByUser(user: User): Long {
        return notificationRepository.countByUserIdAndReadFalse(user.id!!)
    }

    fun read(notificationId: ObjectId, user: User): Notification {
        val notification = notificationRepository.findByIdOrNull(notificationId)
            ?: throw DoriDoriException.of(DoriDoriExceptionType.NOT_EXIST)

        if (notification.userId != user.id) {
            throw DoriDoriException.of(DoriDoriExceptionType.NOT_ALLOWED_TO_ACCESS)
        }

        return notificationRepository.save(notification.read())
    }
}
