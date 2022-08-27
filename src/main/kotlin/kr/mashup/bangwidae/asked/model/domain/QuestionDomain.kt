package kr.mashup.bangwidae.asked.model.domain

import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.model.document.question.Answer
import kr.mashup.bangwidae.asked.model.document.question.Question
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class QuestionDomain(
    val id: ObjectId,
    val content: String,
    val representativeAddress: String,
    val anonymous: Boolean,
    val fromUser: WriterUserDomain,
    val toUser: WriterUserDomain,
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
            representativeAddress = question.representativeAddress ?: "",
            anonymous = question.anonymous,
            fromUser = WriterUserDomain.of(fromUser, question.anonymous),
            toUser = WriterUserDomain.of(toUser, false),
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
    val representativeAddress: String,
    val user: WriterUserDomain,
    val likeCount: Long,
    val userLiked: Boolean,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(answer: Answer, user: User, likeCount: Long, userLiked: Boolean) = AnswerDomain(
            id = answer.id!!,
            content = answer.content,
            representativeAddress = answer.representativeAddress ?: "",
            user = WriterUserDomain.of(user, false),
            likeCount = likeCount,
            userLiked = userLiked,
            createdAt = answer.createdAt!!,
        )
    }
}

