package kr.mashup.bangwidae.asked.service.question

import kr.mashup.bangwidae.asked.controller.dto.*
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.question.Answer
import kr.mashup.bangwidae.asked.model.question.AnswerLike
import kr.mashup.bangwidae.asked.model.question.Question
import kr.mashup.bangwidae.asked.model.question.QuestionStatus
import kr.mashup.bangwidae.asked.repository.*
import kr.mashup.bangwidae.asked.service.place.PlaceService
import kr.mashup.bangwidae.asked.utils.GeoUtils
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class QuestionService(
    private val placeService: PlaceService,
    private val questionRepository: QuestionRepository,
    private val answerRepository: AnswerRepository,
    private val answerLikeRepository: AnswerLikeRepository,
    private val answerLikeAggregator: AnswerLikeAggregator,
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

        val users = userRepository
            .findAllByIdIn(questions.map { it.fromUserId } + user.id)

        return QuestionSearchResultImpl(
            questions = questions,
            userMapByUserId = users.associateBy { it.id!! },
        )
    }

    fun countAnswerWaitingByToUser(user: User): Long {
        return questionRepository.countByToUserIdAndStatusAndDeletedFalse(
            toUserId = user.id!!,
            status = QuestionStatus.ANSWER_WAITING,
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

        val answers = answerRepository
            .findByQuestionIdInAndDeletedFalseOrderByCreatedAtDesc(questions.map { it.id!! })

        val users = userRepository
            .findAllByIdIn(questions.map { it.fromUserId } + user.id)

        val answerLikeCountMapByAnswerId = answerLikeAggregator
            .getCountGroupByAnswerId(answers.map { it.id!! })

        val myAnswerLikes = answerLikeRepository
            .findByAnswerIdInAndUserId(answers.map { it.id!! }, user.id)

        return QuestionSearchResultWithAnswerImpl(
            questions = questions,
            userMapByUserId = users.associateBy { it.id!! },
            answerMapByQuestionId = answers.groupBy { it.questionId },
            answerLikeCountMapByAnswerId = answerLikeCountMapByAnswerId,
            userAnswerLikeMapByAnswerId = myAnswerLikes.associateBy { it.answerId }
        )
    }

    fun findByFromUser(user: User, lastId: ObjectId?, size: Int): QuestionSearchResultImpl {
        val questions = questionRepository.findByFromUserIdAndIdBeforeAndDeletedFalseOrderByCreatedAtDesc(
            fromUserId = user.id!!,
            lastId = lastId ?: ObjectId(),
            pageRequest = PageRequest.of(0, size)
        )

        val users = userRepository
            .findAllByIdIn(questions.map { it.toUserId } + user.id)

        return QuestionSearchResultImpl(
            questions = questions,
            userMapByUserId = users.associateBy { it.id!! },
        )
    }

    fun write(user: User, request: QuestionWriteRequest): Question {
        userRepository.findByIdOrNull(request.toUserId)
            ?: throw DoriDoriException.of(
                type = DoriDoriExceptionType.NOT_EXIST,
                message = "${request.toUserId} 사용자가 존재하지 않아요",
            )

        runCatching {
            placeService.reverseGeocode(
                longitude = request.longitude,
                latitude = request.latitude
            )
        }.getOrNull().let {
            return questionRepository.save(
                Question(
                    fromUserId = user.id!!,
                    toUserId = request.toUserId,
                    content = request.content,
                    anonymous = request.anonymous,
                    location = GeoUtils.geoJsonPoint(longitude = request.longitude, latitude = request.latitude),
                    representativeAddress = it?.representativeAddress,
                    region = it,
                )
            )
        }
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
    val answerLikeCountMapByAnswerId: Map<ObjectId, Long> = emptyMap(),
    val userAnswerLikeMapByAnswerId: Map<ObjectId, AnswerLike> = emptyMap(),
) : QuestionSearchResult
