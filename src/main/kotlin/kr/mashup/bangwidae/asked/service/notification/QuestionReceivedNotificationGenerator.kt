package kr.mashup.bangwidae.asked.service.notification

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.Notification
import kr.mashup.bangwidae.asked.service.event.NotificationEvent
import kr.mashup.bangwidae.asked.service.event.QuestionWriteEvent
import kr.mashup.bangwidae.asked.service.question.QuestionService
import kr.mashup.bangwidae.asked.utils.StringUtils
import kr.mashup.bangwidae.asked.utils.UrlSchemeParameter
import kr.mashup.bangwidae.asked.utils.UrlSchemeUtils
import org.springframework.stereotype.Component

@Component
class QuestionReceivedNotificationGenerator(
    private val questionService: QuestionService,
    private val urlSchemeProperties: UrlSchemeProperties,
) : NotificationGenerator {
    override fun support(event: NotificationEvent): Boolean {
        return event is QuestionWriteEvent
    }

    override fun generate(event: NotificationEvent): List<Notification> {
        if (event is QuestionWriteEvent) {
            val question = questionService.findDetailById(null, event.questionId)

            return listOf(
                Notification(
                    type = NotificationType.QUESTION_RECEIVED,
                    userId = question.toUser.id,
                    title = "질문: ${StringUtils.ellipsis(question.content, 10)}",
                    content = "${question.fromUser.nickname}의 질문을 확인해보세요.",
                    urlScheme = UrlSchemeUtils.generate(
                        urlSchemeProperties.questionDetail,
                        UrlSchemeParameter("questionId", question.id.toHexString())
                    )
                )
            )
        } else {
            throw DoriDoriException.of(DoriDoriExceptionType.SYSTEM_FAIL)
        }
    }
}
