package kr.mashup.bangwidae.asked.controller.dto

import io.swagger.annotations.ApiModelProperty
import kr.mashup.bangwidae.asked.model.domain.AnswerDomain
import kr.mashup.bangwidae.asked.model.domain.QuestionDomain
import kr.mashup.bangwidae.asked.model.domain.QuestionUserDomain
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
data class QuestionDetailDto(
    val id: String,
    val content: String,
    val representativeAddress: String?,
    val anonymous: Boolean,
    val fromUser: QuestionUserDto,
    val toUser: QuestionUserDto,
    val answer: AnswerDto?,
    val createdAt: LocalDateTime,
) {
    data class AnswerDto(
        val id: String,
        val content: String,
        val representativeAddress: String?,
        val user: QuestionUserDto,
        val likeCount: Long,
        val userLiked: Boolean,
        val createdAt: LocalDateTime,
    ) {
        companion object {
            fun from(answer: AnswerDomain): AnswerDto {
                return AnswerDto(
                    id = answer.id.toHexString(),
                    content = answer.content,
                    representativeAddress = answer.representativeAddress,
                    user = QuestionUserDto.from(answer.user),
                    likeCount = answer.likeCount,
                    userLiked = answer.userLiked,
                    createdAt = answer.createdAt,
                )
            }
        }
    }

    companion object {
        fun from(question: QuestionDomain): QuestionDetailDto {
            return QuestionDetailDto(
                id = question.id.toHexString(),
                content = question.content,
                representativeAddress = question.representativeAddress,
                anonymous = question.anonymous,
                fromUser = QuestionUserDto.from(question.fromUser),
                toUser = QuestionUserDto.from(question.toUser),
                answer = question.answer?.let { AnswerDto.from(question.answer) },
                createdAt = question.createdAt,
            )
        }
    }
}

data class AnsweredQuestionsDto(
    val questions: List<QuestionDto>,
    val hasNext: Boolean,
) {
    data class QuestionDto(
        val id: String,
        val content: String,
        val representativeAddress: String?,
        val anonymous: Boolean,
        val fromUser: QuestionUserDto,
        val toUser: QuestionUserDto,
        val answer: AnswerDto,
        val createdAt: LocalDateTime,
    ) {
        companion object {
            fun from(question: QuestionDomain): QuestionDto {
                return QuestionDto(
                    id = question.id.toHexString(),
                    content = question.content,
                    representativeAddress = question.representativeAddress,
                    anonymous = question.anonymous,
                    fromUser = QuestionUserDto.from(question.fromUser),
                    toUser = QuestionUserDto.from(question.toUser),
                    answer = AnswerDto.from(question.answer!!),
                    createdAt = question.createdAt,
                )
            }
        }
    }

    data class AnswerDto(
        val id: String,
        val content: String,
        val representativeAddress: String?,
        val user: QuestionUserDto,
        val likeCount: Long,
        val userLiked: Boolean,
        val createdAt: LocalDateTime,
    ) {
        companion object {
            fun from(answer: AnswerDomain): AnswerDto {
                return AnswerDto(
                    id = answer.id.toHexString(),
                    content = answer.content,
                    representativeAddress = answer.representativeAddress,
                    user = QuestionUserDto.from(answer.user),
                    likeCount = answer.likeCount,
                    userLiked = answer.userLiked,
                    createdAt = answer.createdAt,
                )
            }
        }
    }

    companion object {
        fun from(
            questions: List<QuestionDomain>,
            requestedSize: Int,
        ): AnsweredQuestionsDto {
            return AnsweredQuestionsDto(
                questions = questions
                    .subList(0, min(questions.size, requestedSize))
                    .map { QuestionDto.from(it) },
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
        val anonymous: Boolean,
        val fromUser: QuestionUserDto,
        val toUser: QuestionUserDto,
        val createdAt: LocalDateTime,
    ) {
        companion object {
            fun from(question: QuestionDomain): QuestionDto {
                return QuestionDto(
                    id = question.id.toHexString(),
                    content = question.content,
                    representativeAddress = question.representativeAddress,
                    anonymous = question.anonymous,
                    fromUser = QuestionUserDto.from(question.fromUser),
                    toUser = QuestionUserDto.from(question.toUser),
                    createdAt = question.createdAt,
                )
            }
        }
    }

    companion object {
        fun from(
            questions: List<QuestionDomain>,
            requestedSize: Int,
        ): ReceivedQuestionsDto {
            return ReceivedQuestionsDto(
                questions = questions.subList(0, min(questions.size, requestedSize))
                    .subList(0, min(questions.size, requestedSize))
                    .map { QuestionDto.from(it) },
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
        val anonymous: Boolean,
        val fromUser: QuestionUserDto,
        val toUser: QuestionUserDto,
        val createdAt: LocalDateTime,
    ) {
        companion object {
            fun from(question: QuestionDomain): QuestionDto {
                return QuestionDto(
                    id = question.id.toHexString(),
                    content = question.content,
                    representativeAddress = question.representativeAddress,
                    anonymous = question.anonymous,
                    fromUser = QuestionUserDto.from(question.fromUser),
                    toUser = QuestionUserDto.from(question.toUser),
                    createdAt = question.createdAt,
                )
            }
        }
    }

    companion object {
        fun from(
            questions: List<QuestionDomain>,
            requestedSize: Int,
        ): AskedQuestionsDto {
            return AskedQuestionsDto(
                questions = questions.subList(0, min(questions.size, requestedSize))
                    .subList(0, min(questions.size, requestedSize))
                    .map { QuestionDto.from(it) },
                hasNext = questions.size > requestedSize
            )
        }
    }
}

data class QuestionUserDto(
    val id: String,
    val nickname: String,
    val tags: List<String>,
    val profileImageUrl: String?,
    val level: Int
) {
    companion object {
        fun from(user: QuestionUserDomain): QuestionUserDto {
            return QuestionUserDto(
                id = user.id.toHexString(),
                nickname = user.nickname,
                tags = user.tags,
                profileImageUrl = user.profileImageUrl,
                level = user.level
            )
        }
    }
}
