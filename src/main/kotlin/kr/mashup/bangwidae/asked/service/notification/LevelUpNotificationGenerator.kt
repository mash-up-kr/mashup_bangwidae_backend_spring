package kr.mashup.bangwidae.asked.service.notification

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.document.Notification
import kr.mashup.bangwidae.asked.service.UserService
import kr.mashup.bangwidae.asked.service.event.LevelUpEvent
import kr.mashup.bangwidae.asked.service.event.NotificationEvent
import org.springframework.stereotype.Component

@Component
class LevelUpNotificationGenerator(
    private val userService: UserService,
    private val urlSchemeProperties: UrlSchemeProperties,
) : NotificationGenerator {
    override fun support(event: NotificationEvent): Boolean {
        return event is LevelUpEvent
    }

    override fun generate(event: NotificationEvent): List<Notification> {
        if (event is LevelUpEvent) {
            val user = userService.findById(event.userId)

            return listOf(
                Notification(
                    type = NotificationType.LEVEL_UP,
                    userId = user.id!!,
                    title = "Level ${user.level} 달성",
                    content = "이제 새로운 와드를 지정할 수 있어요.",
                    urlScheme = urlSchemeProperties.userLevelUp
                )
            )
        } else {
            throw DoriDoriException.of(DoriDoriExceptionType.SYSTEM_FAIL)
        }
    }
}
