package kr.mashup.bangwidae.asked.service.alarm

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.Notification
import kr.mashup.bangwidae.asked.service.UserService
import kr.mashup.bangwidae.asked.utils.UrlSchemeParameter
import kr.mashup.bangwidae.asked.utils.UrlSchemeUtils
import org.springframework.stereotype.Component

@Component
class LevelUpNotificationGenerator(
    private val userService: UserService,
    private val urlSchemeProperties: UrlSchemeProperties,
) : NotificationGenerator {
    override fun support(spec: NotificationSpec): Boolean {
        return spec is LevelUpNotificationSpec
    }

    override fun generate(spec: NotificationSpec): List<Notification> {
        if (spec is LevelUpNotificationSpec) {
            val user = userService.getUserInfo(spec.userId)

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