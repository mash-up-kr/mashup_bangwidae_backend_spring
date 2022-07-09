package kr.mashup.bangwidae.asked.service.question

import kr.mashup.bangwidae.asked.controller.dto.QuestionEditRequest
import kr.mashup.bangwidae.asked.controller.dto.QuestionWriteRequest
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.question.Question
import kr.mashup.bangwidae.asked.repository.QuestionRepository
import kr.mashup.bangwidae.asked.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class QuestionService(
    private val questionRepository: QuestionRepository,
    private val userRepository: UserRepository,
) : WithQuestionAuthorityValidator {
    fun findById(questionId: ObjectId): Question {
        return questionRepository.findByIdAndDeletedFalse(questionId)
            ?: throw DoriDoriException.of(DoriDoriExceptionType.NOT_EXIST)
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
