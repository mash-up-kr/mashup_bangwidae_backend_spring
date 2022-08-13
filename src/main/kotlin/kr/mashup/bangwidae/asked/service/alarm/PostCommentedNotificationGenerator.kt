package kr.mashup.bangwidae.asked.service.alarm

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.Notification
import kr.mashup.bangwidae.asked.service.UserService
import kr.mashup.bangwidae.asked.service.post.CommentService
import kr.mashup.bangwidae.asked.service.post.PostService
import kr.mashup.bangwidae.asked.utils.StringUtils
import org.springframework.stereotype.Component

@Component
class PostCommentedNotificationGenerator(
    private val postService: PostService,
    private val commentService: CommentService,
    private val userService: UserService,
) : NotificationGenerator {
    override fun support(spec: NotificationSpec): Boolean {
        return spec is PostCommentedNotificationSpec
    }

    override fun generate(spec: NotificationSpec): List<Notification> {
        if (spec is PostCommentedNotificationSpec) {
            val comment = commentService.findById(spec.commentId)
            val commentUserNickname =
                if (comment.anonymous == true) "익명" else userService.getUserInfo(comment.userId).nickname
            val post = postService.findById(comment.postId)

            return listOf(
                Notification(
                    type = NotificationType.POST_COMMENTED,
                    userId = post.userId,
                    title = "질문: ${StringUtils.ellipsis(post.content, 10)}",
                    content = "${commentUserNickname}의 답변이 도착했어요!"
                )
            )
        } else {
            throw DoriDoriException.of(DoriDoriExceptionType.SYSTEM_FAIL)
        }
    }
}
