package kr.mashup.bangwidae.asked.service.question

import kr.mashup.bangwidae.asked.controller.dto.AnswerEditRequest
import kr.mashup.bangwidae.asked.controller.dto.AnswerWriteRequest
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.question.Answer
import kr.mashup.bangwidae.asked.model.question.AnswerLike
import kr.mashup.bangwidae.asked.repository.AnswerLikeRepository
import kr.mashup.bangwidae.asked.repository.AnswerRepository
import kr.mashup.bangwidae.asked.repository.QuestionRepository
import kr.mashup.bangwidae.asked.service.event.AnswerWriteEvent
import kr.mashup.bangwidae.asked.service.levelpolicy.LevelPolicyService
import kr.mashup.bangwidae.asked.service.place.PlaceService
import kr.mashup.bangwidae.asked.utils.GeoUtils
import org.bson.types.ObjectId
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class AnswerService(
    private val placeService: PlaceService,
    private val levelPolicyService: LevelPolicyService,
    private val answerRepository: AnswerRepository,
    private val answerLikeRepository: AnswerLikeRepository,
    private val questionRepository: QuestionRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) : WithQuestionAuthorityValidator, WithAnswerAuthorityValidator {
    fun findById(answerId: ObjectId): Answer {
        return answerRepository.findByIdAndDeletedFalse(answerId)
            ?: throw DoriDoriException.of(DoriDoriExceptionType.NOT_EXIST)
    }

    fun write(user: User, questionId: ObjectId, request: AnswerWriteRequest): Answer {
        val question = questionRepository.findByIdAndDeletedFalse(questionId)
            ?.also { it.validateToAnswer(user) }
            ?: throw DoriDoriException.of(DoriDoriExceptionType.NOT_EXIST)

        questionRepository.save(
            question.answer()
        )

        runCatching {
            placeService.reverseGeocode(
                longitude = request.longitude,
                latitude = request.latitude
            )
        }.getOrNull().let {
            return answerRepository.save(
                Answer(
                    userId = user.id!!,
                    questionId = questionId,
                    content = request.content,
                    location = GeoUtils.geoJsonPoint(longitude = request.longitude, latitude = request.latitude),
                    representativeAddress = it?.representativeAddress,
                    region = it,
                )
            ).also {
                levelPolicyService.levelUpIfConditionSatisfied(user)
            }.also {
                applicationEventPublisher.publishEvent(AnswerWriteEvent(it.id!!))
            }
        }
    }

    fun edit(user: User, answerId: ObjectId, request: AnswerEditRequest): Answer {
        val answer = findById(answerId)
            .also { it.validateToUpdate(user) }

        return answerRepository.save(
            answer.updateContent(request.content)
        )
    }

    fun delete(user: User, answerId: ObjectId): Answer {
        val answer = findById(answerId)
            .also { it.validateToDelete(user) }

        return answerRepository.save(
            answer.delete()
        ).also {
            if (answerDoesNotExist(it.questionId)) {
                makeQuestionWaiting(it.questionId)
            }
        }
    }

    fun answerLike(answerId: ObjectId, user: User) {
        require(answerRepository.existsByIdAndDeletedFalse(answerId)) {
            throw DoriDoriException.of(DoriDoriExceptionType.NOT_EXIST)
        }
        if (!answerLikeRepository.existsByAnswerIdAndUserId(answerId, user.id!!)) {
            answerLikeRepository.save(AnswerLike(userId = user.id, answerId = answerId))
        }
    }

    fun answerUnlike(answerId: ObjectId, userId: ObjectId) {
        answerLikeRepository.findByAnswerIdAndUserId(answerId, userId)?.let {
            answerLikeRepository.delete(it)
        }
    }

    private fun answerDoesNotExist(questionId: ObjectId): Boolean {
        return answerRepository.countByQuestionIdAndDeletedFalse(questionId) == 0L
    }

    private fun makeQuestionWaiting(questionId: ObjectId) {
        val question = questionRepository.findByIdOrNull(questionId)
            ?: throw DoriDoriException.of(DoriDoriExceptionType.NOT_EXIST)

        questionRepository.save(
            question.waiting()
        )
    }
}
