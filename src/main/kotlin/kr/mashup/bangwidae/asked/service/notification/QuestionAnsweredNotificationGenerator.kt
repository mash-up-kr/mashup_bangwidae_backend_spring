package kr.mashup.bangwidae.asked.service.notification

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.service.event.AnswerWriteEvent
import kr.mashup.bangwidae.asked.service.event.NotificationEvent
import kr.mashup.bangwidae.asked.model.document.Notification
import kr.mashup.bangwidae.asked.service.question.AnswerService
import kr.mashup.bangwidae.asked.service.question.QuestionService
import kr.mashup.bangwidae.asked.utils.StringUtils
import kr.mashup.bangwidae.asked.utils.UrlSchemeParameter
import kr.mashup.bangwidae.asked.utils.UrlSchemeUtils
import org.springframework.stereotype.Component

@Component
class QuestionAnsweredNotificationGenerator(
    private val questionService: QuestionService,
    private val answerService: AnswerService,
    private val urlSchemeProperties: UrlSchemeProperties,
) : NotificationGenerator {
    override fun support(event: NotificationEvent): Boolean {
        return event is AnswerWriteEvent
    }

    override fun generate(event: NotificationEvent): List<Notification> {
        if (event is AnswerWriteEvent) {
            val answer = answerService.findById(event.answerId)
            val question = questionService.findDetailById(null, answer.questionId)

            return listOf(
                Notification(
                    type = NotificationType.QUESTION_ANSWERED,
                    userId = question.fromUser.id,
                    title = "질문: ${StringUtils.ellipsis(question.content, 10)}",
                    content = "${question.toUser.nickname}의 답변이 도착했어요!",
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
