package kr.mashup.bangwidae.asked.service.question

import kr.mashup.bangwidae.asked.controller.dto.*
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.model.document.question.Question
import kr.mashup.bangwidae.asked.model.document.question.QuestionStatus
import kr.mashup.bangwidae.asked.model.domain.QuestionDomain
import kr.mashup.bangwidae.asked.repository.*
import kr.mashup.bangwidae.asked.service.event.QuestionWriteEvent
import kr.mashup.bangwidae.asked.service.levelpolicy.LevelPolicyService
import kr.mashup.bangwidae.asked.service.place.PlaceService
import kr.mashup.bangwidae.asked.utils.GeoUtils
import org.bson.types.ObjectId
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class QuestionService(
    private val placeService: PlaceService,
    private val levelPolicyService: LevelPolicyService,
    private val questionRepository: QuestionRepository,
    private val answerRepository: AnswerRepository,
    private val answerLikeRepository: AnswerLikeRepository,
    private val answerLikeAggregator: AnswerLikeAggregator,
    private val userRepository: UserRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) : WithQuestionAuthorityValidator {
    private fun findById(questionId: ObjectId): Question {
        return questionRepository.findByIdAndDeletedFalse(questionId)
            ?: throw DoriDoriException.of(DoriDoriExceptionType.NOT_EXIST)
    }

    fun findDetailById(authUser: User?, questionId: ObjectId): QuestionDomain {
        val question = findById(questionId)

        return question.toDomain(authUser)
    }

    fun findAnswerWaitingByToUser(toUser: User, lastId: ObjectId?, size: Int): List<QuestionDomain> {
        val questions = questionRepository.findByToUserIdAndStatusAndIdBeforeAndDeletedFalseOrderByCreatedAtDesc(
            toUserId = toUser.id!!,
            status = QuestionStatus.ANSWER_WAITING,
            lastId = lastId ?: ObjectId(),
            pageRequest = PageRequest.of(0, size)
        )
        return questions.toDomain(toUser)
    }

    fun countAnswerWaitingByToUser(toUser: User): Long {
        return questionRepository.countByToUserIdAndStatusAndDeletedFalse(
            toUserId = toUser.id!!,
            status = QuestionStatus.ANSWER_WAITING,
        )
    }

    fun findAnswerCompleteByToUser(authUser: User?, toUserID: ObjectId, lastId: ObjectId?, size: Int): List<QuestionDomain> {
        val toUser = userRepository.findByIdOrNull(toUserID)
            ?: throw DoriDoriException.of(
                type = DoriDoriExceptionType.NOT_EXIST,
                message = "$toUserID 사용자가 존재하지 않아요",
            )

        return findAnswerCompleteByToUser(
            authUser = authUser,
            toUser = toUser,
            lastId = lastId,
            size = size,
        )
    }

    fun findAnswerCompleteByToUser(authUser: User?, toUser: User, lastId: ObjectId?, size: Int): List<QuestionDomain> {
        val questions = questionRepository.findByToUserIdAndStatusAndIdBeforeAndDeletedFalseOrderByCreatedAtDesc(
            toUserId = toUser.id!!,
            status = QuestionStatus.ANSWER_COMPLETE,
            lastId = lastId ?: ObjectId(),
            pageRequest = PageRequest.of(0, size)
        )

        return questions.toDomain(authUser)
    }

    fun findByFromUser(fromUser: User, lastId: ObjectId?, size: Int): List<QuestionDomain> {
        val questions = questionRepository.findByFromUserIdAndIdBeforeAndDeletedFalseOrderByCreatedAtDesc(
            fromUserId = fromUser.id!!,
            lastId = lastId ?: ObjectId(),
            pageRequest = PageRequest.of(0, size)
        )

        return questions.toDomain(fromUser)
    }

    private fun Question.toDomain(authUser: User?): QuestionDomain {
        return listOf(this).toDomain(authUser).first()
    }

    private fun List<Question>.toDomain(authUser: User?): List<QuestionDomain> {
        val answers = answerRepository
            .findByQuestionIdInAndDeletedFalseOrderByCreatedAtDesc(this.map { it.id!! })
        val answersMapByQuestionId = answers.groupBy { it.questionId }

        val userMapByUserId = userRepository
            .findAllByIdIn((this.map { it.fromUserId } + this.map { it.toUserId }).toSet())
            .associateBy { it.id!! }

        val answerLikeCountMapByAnswerId = answerLikeAggregator.getCountGroupByAnswerId(answers.map { it.id!! })

        val myAnswerLikeMapByAnswerId = authUser?.let {
            answerLikeRepository
                .findByAnswerIdInAndUserId(answers.map { it.id!! }, authUser.id!!)
                .associateBy { it.answerId }
        } ?: emptyMap()

        return this.map {
            val answer = answersMapByQuestionId[it.id]?.firstOrNull()
            QuestionDomain.from(
                question = it,
                fromUser = userMapByUserId[it.fromUserId]!!,
                toUser = userMapByUserId[it.toUserId]!!,
                answer = answer,
                answerLikeCount = answer?.let { answerLikeCountMapByAnswerId[answer.id!!] ?: 0 },
                answerUserLiked = answer?.let { myAnswerLikeMapByAnswerId[answer.id] != null },
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
            ).also {
                levelPolicyService.levelUpIfConditionSatisfied(user)
            }.also {
                applicationEventPublisher.publishEvent(QuestionWriteEvent(it.id!!))
            }
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

