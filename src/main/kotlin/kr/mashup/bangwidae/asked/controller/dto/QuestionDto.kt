package kr.mashup.bangwidae.asked.controller.dto

import io.swagger.annotations.ApiModelProperty
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.question.Answer
import kr.mashup.bangwidae.asked.model.question.Question
import org.bson.types.ObjectId
import java.time.LocalDateTime
import kotlin.math.min

// Request
data class QuestionWriteRequest(
    @ApiModelProperty(value = "질문 내용", example = "질문 내용 string")
    val content: String,
    @ApiModelProperty(value = "질문 대상자 user id", example = "62b49a12507aeb02e6534572")
    val toUserId: ObjectId,
    val longitude: Double,
    val latitude: Double,
    val anonymous: Boolean,
)

data class QuestionEditRequest(
    @ApiModelProperty(value = "질문 내용", example = "질문 내용 string")
    val content: String,
)

data class AnswerWriteRequest(
    @ApiModelProperty(value = "답변 내용", example = "질문 내용 string")
    val content: String,
    val longitude: Double,
    val latitude: Double,
)

data class AnswerEditRequest(
    @ApiModelProperty(value = "질문 내용", example = "질문 내용 string")
    val content: String,
)

// Response
data class AnsweredQuestionsDto(
    val questions: List<QuestionDto>,
    val hasNext: Boolean,
) {
    data class QuestionDto(
        val id: String,
        val content: String,
        val representativeAddress: String?,
        val fromUser: QuestionUserDto,
        val toUser: QuestionUserDto,
        val answer: AnswerDto,
        val createdAt: LocalDateTime,
    ) {
        companion object {
            fun from(question: Question, answer: Answer, fromUser: User, toUser: User): QuestionDto {
                return QuestionDto(
                    id = question.id!!.toHexString(),
                    content = question.content,
                    representativeAddress = question.representativeAddress,
                    fromUser = if (question.anonymous == true) QuestionUserDto.anonymous(fromUser) else QuestionUserDto.from(fromUser),
                    toUser = QuestionUserDto.from(toUser),
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
        val user: QuestionUserDto,
        val likeCount: Int,
        val createdAt: LocalDateTime,
    ) {
        companion object {
            fun from(answer: Answer, answerUser: User): AnswerDto {
                return AnswerDto(
                    id = answer.id!!.toHexString(),
                    content = answer.content,
                    representativeAddress = answer.representativeAddress,
                    user = QuestionUserDto.from(answerUser),
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
        val fromUser: QuestionUserDto,
        val toUser: QuestionUserDto,
        val createdAt: LocalDateTime,
    ) {
        companion object {
            fun from(question: Question, fromUser: User, toUser: User): QuestionDto {
                return QuestionDto(
                    id = question.id!!.toHexString(),
                    content = question.content,
                    representativeAddress = question.representativeAddress,
                    fromUser = if (question.anonymous == true) QuestionUserDto.anonymous(fromUser) else QuestionUserDto.from(fromUser),
                    toUser = QuestionUserDto.from(toUser),
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
        val fromUser: QuestionUserDto,
        val toUser: QuestionUserDto,
        val createdAt: LocalDateTime,
    ) {
        companion object {
            fun from(question: Question, fromUser: User, toUser: User): QuestionDto {
                return QuestionDto(
                    id = question.id!!.toHexString(),
                    content = question.content,
                    representativeAddress = question.representativeAddress,
                    fromUser = if (question.anonymous == true) QuestionUserDto.anonymous(fromUser) else QuestionUserDto.from(fromUser),
                    toUser = QuestionUserDto.from(toUser),
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

data class QuestionUserDto(
    val anonymous: Boolean,
    val id: String,
    val nickname: String,
    val tags: List<String>,
    val profileImageUrl: String?,
) {
    companion object {
        fun from(user: User): QuestionUserDto {
            return QuestionUserDto(
                anonymous = false,
                id = user.id!!.toHexString(),
                nickname = user.nickname!!,
                tags = user.tags,
                profileImageUrl = user.profileImageUrl
            )
        }

        fun anonymous(user: User): QuestionUserDto {
            return QuestionUserDto(
                anonymous = true,
                id = user.id!!.toHexString(),
                nickname = "익명",
                tags = emptyList(),
                // TODO 기본 익명 이미지 Url 로 설정
                profileImageUrl = "default profileImageUrl"
            )
        }
    }
}
