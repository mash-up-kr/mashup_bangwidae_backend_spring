package kr.mashup.bangwidae.asked.service.alarm

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.Notification
import kr.mashup.bangwidae.asked.service.question.QuestionService
import kr.mashup.bangwidae.asked.utils.StringUtils
import org.springframework.stereotype.Component

@Component
class QuestionReceivedNotificationGenerator(
    private val questionService: QuestionService,
) : NotificationGenerator {
    override fun support(spec: NotificationSpec): Boolean {
        return spec is QuestionReceivedNotificationSpec
    }

    override fun generate(spec: NotificationSpec): List<Notification> {
        if (spec is QuestionReceivedNotificationSpec) {
            val question = questionService.findDetailById(null, spec.questionId)

            return listOf(
                Notification(
                    type = NotificationType.QUESTION_RECEIVED,
                    userId = question.toUser.id,
                    title = "질문: ${StringUtils.ellipsis(question.content, 10)}",
                    content = "${question.fromUser.nickname}의 질문을 확인해보세요."
                )
            )
        } else {
            throw DoriDoriException.of(DoriDoriExceptionType.SYSTEM_FAIL)
        }
    }
}
