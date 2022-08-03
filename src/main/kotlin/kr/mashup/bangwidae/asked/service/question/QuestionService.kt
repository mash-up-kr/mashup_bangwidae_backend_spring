package kr.mashup.bangwidae.asked.service.question

import kr.mashup.bangwidae.asked.controller.dto.*
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.User
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

    fun findAnswerWaitingByToUser(user: User, lastId: ObjectId?, size: Int): List<QuestionDomain> {
        val questions = questionRepository.findByToUserIdAndStatusAndIdBeforeAndDeletedFalseOrderByCreatedAtDesc(
            toUserId = user.id!!,
            status = QuestionStatus.ANSWER_WAITING,
            lastId = lastId ?: ObjectId(),
            pageRequest = PageRequest.of(0, size)
        )

        val userMapByUserId = userRepository
            .findAllByIdIn(questions.map { it.fromUserId } + user.id)
            .associateBy { it.id!! }

        return questions.map {
            QuestionDomain.from(
                question = it,
                fromUser = userMapByUserId[it.fromUserId]!!,
                toUser = userMapByUserId[it.toUserId]!!,
            )
        }
    }

    fun countAnswerWaitingByToUser(user: User): Long {
        return questionRepository.countByToUserIdAndStatusAndDeletedFalse(
            toUserId = user.id!!,
            status = QuestionStatus.ANSWER_WAITING,
        )
    }

    fun findAnswerCompleteByToUser(userId: ObjectId, lastId: ObjectId?, size: Int): List<QuestionDomain> {
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

    fun findAnswerCompleteByToUser(user: User, lastId: ObjectId?, size: Int): List<QuestionDomain> {
        val questions = questionRepository.findByToUserIdAndStatusAndIdBeforeAndDeletedFalseOrderByCreatedAtDesc(
            toUserId = user.id!!,
            status = QuestionStatus.ANSWER_COMPLETE,
            lastId = lastId ?: ObjectId(),
            pageRequest = PageRequest.of(0, size)
        )

        val answers = answerRepository
            .findByQuestionIdInAndDeletedFalseOrderByCreatedAtDesc(questions.map { it.id!! })
        val answersMapByQuestionId = answers.groupBy { it.questionId }

        val userMapByUserId = userRepository
            .findAllByIdIn(questions.map { it.fromUserId } + user.id)
            .associateBy { it.id!! }

        val answerLikeCountMapByAnswerId = answerLikeAggregator
            .getCountGroupByAnswerId(answers.map { it.id!! })

        val myAnswerLikeMapByAnswerId = answerLikeRepository
            .findByAnswerIdInAndUserId(answers.map { it.id!! }, user.id)
            .associateBy { it.answerId }

        return questions.map {
            val answer = answersMapByQuestionId[it.id]!!.first()
            QuestionDomain.from(
                question = it,
                answer = answer,
                fromUser = userMapByUserId[it.fromUserId]!!,
                toUser = userMapByUserId[it.toUserId]!!,
                answerLikeCount = answerLikeCountMapByAnswerId[answer.id!!] ?: 0,
                answerUserLiked = myAnswerLikeMapByAnswerId[answer.id] != null,
            )
        }
    }

    fun findByFromUser(user: User, lastId: ObjectId?, size: Int): List<QuestionDomain> {
        val questions = questionRepository.findByFromUserIdAndIdBeforeAndDeletedFalseOrderByCreatedAtDesc(
            fromUserId = user.id!!,
            lastId = lastId ?: ObjectId(),
            pageRequest = PageRequest.of(0, size)
        )

        val userMapByUserId = userRepository
            .findAllByIdIn(questions.map { it.toUserId } + user.id)
            .associateBy { it.id!! }

        return questions.map {
            QuestionDomain.from(
                question = it,
                fromUser = userMapByUserId[it.fromUserId]!!,
                toUser = userMapByUserId[it.toUserId]!!,
            )
        }
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

