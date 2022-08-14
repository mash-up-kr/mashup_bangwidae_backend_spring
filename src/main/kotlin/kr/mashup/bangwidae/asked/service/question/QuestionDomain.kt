package kr.mashup.bangwidae.asked.service.question

import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.question.Answer
import kr.mashup.bangwidae.asked.model.question.Question
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class QuestionDomain(
    val id: ObjectId,
    val content: String,
    val representativeAddress: String?,
    val anonymous: Boolean?,
    val fromUser: QuestionUserDomain,
    val toUser: QuestionUserDomain,
    val answer: AnswerDomain?,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(
            question: Question,
            fromUser: User,
            toUser: User,
            answer: Answer? = null,
            answerLikeCount: Long? = null,
            answerUserLiked: Boolean? = null,
        ) = QuestionDomain(
            id = question.id!!,
            content = question.content,
            representativeAddress = question.representativeAddress,
            anonymous = question.anonymous,
            fromUser = if (question.anonymous == true) QuestionUserDomain.from(fromUser.getAnonymousUser())
            else QuestionUserDomain.from(fromUser),
            toUser = QuestionUserDomain.from(toUser),
            answer = answer?.let {
                AnswerDomain.from(
                    answer = it,
                    user = toUser,
                    likeCount = answerLikeCount!!,
                    userLiked = answerUserLiked!!,
                )
            },
            createdAt = question.createdAt!!,
        )
    }
}

data class AnswerDomain(
    val id: ObjectId,
    val content: String,
    val representativeAddress: String?,
    val user: QuestionUserDomain,
    val likeCount: Long,
    val userLiked: Boolean,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(answer: Answer, user: User, likeCount: Long, userLiked: Boolean) = AnswerDomain(
            id = answer.id!!,
            content = answer.content,
            representativeAddress = answer.representativeAddress,
            user = QuestionUserDomain.from(user),
            likeCount = likeCount,
            userLiked = userLiked,
            createdAt = answer.createdAt!!,
        )
    }
}

data class QuestionUserDomain(
    val id: ObjectId,
    val nickname: String,
    val tags: List<String>,
    val profileImageUrl: String?,
    val level: Int
) {
    companion object {
        fun from(user: User) = QuestionUserDomain(
            id = user.id!!,
            nickname = user.nickname!!,
            tags = user.tags,
            profileImageUrl = user.userProfileImageUrl,
            level = user.level
        )
    }
}
