package kr.mashup.bangwidae.asked.service.question

import kr.mashup.bangwidae.asked.controller.dto.*
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.question.Answer
import kr.mashup.bangwidae.asked.model.question.Question
import kr.mashup.bangwidae.asked.model.question.QuestionStatus
import kr.mashup.bangwidae.asked.repository.AnswerRepository
import kr.mashup.bangwidae.asked.repository.QuestionRepository
import kr.mashup.bangwidae.asked.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class QuestionService(
    private val questionRepository: QuestionRepository,
    private val answerRepository: AnswerRepository,
    private val userRepository: UserRepository,
) : WithQuestionAuthorityValidator {
    fun findById(questionId: ObjectId): Question {
        return questionRepository.findByIdAndDeletedFalse(questionId)
            ?: throw DoriDoriException.of(DoriDoriExceptionType.NOT_EXIST)
    }

    fun findAnswerWaitingByToUser(user: User, lastId: ObjectId?, size: Int): QuestionSearchResultImpl {
        val questions = questionRepository.findByToUserIdAndStatusAndIdBeforeAndDeletedFalseOrderByCreatedAtDesc(
            toUserId = user.id!!,
            status = QuestionStatus.ANSWER_WAITING,
            lastId = lastId ?: ObjectId(),
            pageRequest = PageRequest.of(0, size)
        )

        val userMapByUserId = userRepository
            .findAllByIdIn(questions.map { it.fromUserId } + user.id)
            .associateBy { it.id!! }

        return QuestionSearchResultImpl(
            questions = questions,
            userMapByUserId = userMapByUserId,
        )
    }

    fun findAnswerCompleteByToUser(userId: ObjectId, lastId: ObjectId?, size: Int): QuestionSearchResultWithAnswerImpl {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw DoriDoriException.of(
                type = DoriDoriExceptionType.NOT_EXIST,
                message = "$userId 사용자가 존재하지 않아요",
            )

        return findAnswerCompleteByToUser(
            user = user,
            lastId = lastId,
            size = size,
        )
    }

    fun findAnswerCompleteByToUser(user: User, lastId: ObjectId?, size: Int): QuestionSearchResultWithAnswerImpl {
        val questions = questionRepository.findByToUserIdAndStatusAndIdBeforeAndDeletedFalseOrderByCreatedAtDesc(
            toUserId = user.id!!,
            status = QuestionStatus.ANSWER_COMPLETE,
            lastId = lastId ?: ObjectId(),
            pageRequest = PageRequest.of(0, size)
        )

        val answerMapByQuestionId = answerRepository
            .findByQuestionIdInAndDeletedFalseOrderByCreatedAtDesc(questions.map { it.id!! })
            .groupBy { it.questionId }

        val userMapByUserId = userRepository
            .findAllByIdIn(questions.map { it.fromUserId } + user.id)
            .associateBy { it.id!! }

        return QuestionSearchResultWithAnswerImpl(
            questions = questions,
            userMapByUserId = userMapByUserId,
            answerMapByQuestionId = answerMapByQuestionId,
        )
    }

    fun write(user: User, request: QuestionWriteRequest): Question {
        userRepository.findByIdOrNull(request.toUserId)
            ?: throw DoriDoriException.of(
                type = DoriDoriExceptionType.NOT_EXIST,
                message = "${request.toUserId} 사용자가 존재하지 않아요",
            )

        return questionRepository.save(
            Question(
                fromUserId = user.id!!,
                toUserId = request.toUserId,
                content = request.content,
            )
        )
    }

    fun edit(user: User, questionId: ObjectId, request: QuestionEditRequest): Question {
        val question = findById(questionId)
            .also { it.validateToUpdate(user) }

        return questionRepository.save(
            question.updateContent(request.content)
        )
    }

    fun delete(user: User, questionId: ObjectId): Question {
        val question = findById(questionId)
            .also { it.validateToDelete(user) }

        return questionRepository.save(
            question.delete()
        )
    }

    fun deny(user: User, questionId: ObjectId): Question {
        val question = findById(questionId)
            .also { it.validateToDeny(user) }

        return questionRepository.save(
            question.deny()
        )
    }
}

interface QuestionSearchResult {
    val questions: List<Question>
    val userMapByUserId: Map<ObjectId, User>
}

data class QuestionSearchResultImpl(
    override val questions: List<Question>,
    override val userMapByUserId: Map<ObjectId, User> = emptyMap(),
) : QuestionSearchResult

data class QuestionSearchResultWithAnswerImpl(
    override val questions: List<Question>,
    override val userMapByUserId: Map<ObjectId, User> = emptyMap(),
    val answerMapByQuestionId: Map<ObjectId, List<Answer>> = emptyMap(),
) : QuestionSearchResult
