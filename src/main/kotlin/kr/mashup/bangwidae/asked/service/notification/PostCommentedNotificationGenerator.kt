package kr.mashup.bangwidae.asked.service.notification

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.Notification
import kr.mashup.bangwidae.asked.service.UserService
import kr.mashup.bangwidae.asked.service.event.CommentWriteEvent
import kr.mashup.bangwidae.asked.service.event.NotificationEvent
import kr.mashup.bangwidae.asked.service.post.CommentService
import kr.mashup.bangwidae.asked.service.post.PostService
import kr.mashup.bangwidae.asked.utils.StringUtils
import kr.mashup.bangwidae.asked.utils.UrlSchemeParameter
import kr.mashup.bangwidae.asked.utils.UrlSchemeUtils
import org.springframework.stereotype.Component

@Component
class PostCommentedNotificationGenerator(
    private val postService: PostService,
    private val commentService: CommentService,
    private val userService: UserService,
    private val urlSchemeProperties: UrlSchemeProperties,
) : NotificationGenerator {
    override fun support(event: NotificationEvent): Boolean {
        return event is CommentWriteEvent
    }

    override fun generate(event: NotificationEvent): List<Notification> {
        if (event is CommentWriteEvent) {
            val comment = commentService.findById(event.commentId)
            val commentUserNickname =
                if (comment.anonymous == true) "익명" else userService.findById(comment.userId).nickname
            val post = postService.findById(comment.postId)

            return listOf(
                Notification(
                    type = NotificationType.POST_COMMENTED,
                    userId = post.userId,
                    title = "질문: ${StringUtils.ellipsis(post.content, 10)}",
                    content = "${commentUserNickname}의 답변이 도착했어요!",
                    urlScheme = UrlSchemeUtils.generate(
                        urlSchemeProperties.postDetail,
                        UrlSchemeParameter("postId", post.id!!.toHexString())
                    )
                )
            )
        } else {
            throw DoriDoriException.of(DoriDoriExceptionType.SYSTEM_FAIL)
        }
    }
}
