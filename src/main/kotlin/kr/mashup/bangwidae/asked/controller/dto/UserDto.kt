package kr.mashup.bangwidae.asked.controller.dto

import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.question.Answer
import kr.mashup.bangwidae.asked.model.question.Question
import org.bson.types.ObjectId
import java.time.LocalDateTime
import kotlin.math.min

data class UserDto(
    val id: String,
    val nickname: String,
    val tags: List<String>,
) {
    companion object {
        fun from(user: User): UserDto {
            return UserDto(
                id = user.id!!.toHexString(),
                nickname = user.nickname!!,
                tags = user.tags,
            )
        }
    }
}

data class JoinUserRequest(
    val email: String,
    val password: String
)

data class JoinUserResponse(
    val accessToken: String,
    val refreshToken: String
)

data class UpdateNicknameRequest(
    val nickname: String
)

data class UpdateProfileRequest(
    val description: String,
    val tags: List<String>,
)

data class AnsweredQuestionsDto(
    val questions: List<QuestionDto>,
    val hasNext: Boolean,
) {
    data class QuestionDto(
        val id: String,
        val content: String,
        val representativeAddress: String,
        val fromUser: UserDto,
        val toUser: UserDto,
        val answer: AnswerDto,
        val createdAt: LocalDateTime,
    ) {
        companion object {
            fun from(question: Question, answer: Answer, fromUser: User, toUser: User): QuestionDto {
                return QuestionDto(
                    id = question.id!!.toHexString(),
                    content = question.content,
                    // TODO Question 에 주소 저장
                    representativeAddress = "대표 주소",
                    fromUser = UserDto.from(fromUser),
                    toUser = UserDto.from(toUser),
                    answer = AnswerDto.from(answer, toUser),
                    createdAt = question.createdAt!!,
                )
            }
        }
    }

    data class AnswerDto(
        val id: String,
        val content: String,
        val representativeAddress: String?,
        val user: UserDto,
        val likeCount: Int,
        val createdAt: LocalDateTime,
    ) {
        companion object {
            fun from(answer: Answer, answerUser: User): AnswerDto {
                return AnswerDto(
                    id = answer.id!!.toHexString(),
                    content = answer.content,
                    representativeAddress = answer.representativeAddress,
                    user = UserDto.from(answerUser),
                    // TODO 좋아요 개수 저장
                    likeCount = 0,
                    createdAt = answer.createdAt!!,
                )
            }
        }
    }

    companion object {
        fun from(
            questions: List<Question>,
            userMapByUserId: Map<ObjectId, User>,
            answerMapByQuestionId: Map<ObjectId, List<Answer>>,
            requestedSize: Int,
        ): AnsweredQuestionsDto {
            return AnsweredQuestionsDto(
                questions = questions.subList(0, min(questions.size, requestedSize))
                    .map {
                        val answer = answerMapByQuestionId[it.id]!!.first()
                        QuestionDto.from(
                            question = it,
                            answer = answer,
                            fromUser = userMapByUserId[it.fromUserId]!!,
                            toUser = userMapByUserId[answer.userId]!!,
                        )
                    },
                hasNext = questions.size > requestedSize
            )
        }
    }
}

data class ReceivedQuestionsDto(
    val questions: List<QuestionDto>,
    val hasNext: Boolean,
) {
    data class QuestionDto(
        val id: String,
        val content: String,
        val representativeAddress: String?,
        val fromUser: UserDto,
        val toUser: UserDto,
        val createdAt: LocalDateTime,
    ) {
        companion object {
            fun from(question: Question, fromUser: User, toUser: User): QuestionDto {
                return QuestionDto(
                    id = question.id!!.toHexString(),
                    content = question.content,
                    representativeAddress = question.representativeAddress,
                    fromUser = UserDto.from(fromUser),
                    toUser = UserDto.from(toUser),
                    createdAt = question.createdAt!!,
                )
            }
        }
    }

    companion object {
        fun from(
            questions: List<Question>,
            userMapByUserId: Map<ObjectId, User>,
            requestedSize: Int,
        ): ReceivedQuestionsDto {
            return ReceivedQuestionsDto(
                questions = questions.subList(0, min(questions.size, requestedSize))
                    .map {
                        QuestionDto.from(
                            question = it,
                            fromUser = userMapByUserId[it.fromUserId]!!,
                            toUser = userMapByUserId[it.toUserId]!!
                        )
                    },
                hasNext = questions.size > requestedSize
            )
        }
    }
}

data class AskedQuestionsDto(
    val questions: List<QuestionDto>,
    val hasNext: Boolean,
) {
    data class QuestionDto(
        val id: String,
        val content: String,
        val representativeAddress: String?,
        val fromUser: UserDto,
        val toUser: UserDto,
        val createdAt: LocalDateTime,
    ) {
        companion object {
            fun from(question: Question, fromUser: User, toUser: User): QuestionDto {
                return QuestionDto(
                    id = question.id!!.toHexString(),
                    content = question.content,
                    representativeAddress = question.representativeAddress,
                    fromUser = UserDto.from(fromUser),
                    toUser = UserDto.from(toUser),
                    createdAt = question.createdAt!!,
                )
            }
        }
    }

    companion object {
        fun from(
            questions: List<Question>,
            userMapByUserId: Map<ObjectId, User>,
            requestedSize: Int,
        ): AskedQuestionsDto {
            return AskedQuestionsDto(
                questions = questions.subList(0, min(questions.size, requestedSize))
                    .map {
                        QuestionDto.from(
                            question = it,
                            fromUser = userMapByUserId[it.fromUserId]!!,
                            toUser = userMapByUserId[it.toUserId]!!
                        )
                    },
                hasNext = questions.size > requestedSize
            )
        }
    }
}
