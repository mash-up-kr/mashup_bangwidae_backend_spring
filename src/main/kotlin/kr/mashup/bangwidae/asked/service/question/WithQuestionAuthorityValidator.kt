package kr.mashup.bangwidae.asked.service.question

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.model.document.question.Question

interface WithQuestionAuthorityValidator {
    fun Question.validateToUpdate(user: User) {
        if (fromUserId != user.id) {
            throw DoriDoriException.of(DoriDoriExceptionType.NOT_ALLOWED_TO_ACCESS)
        }
    }

    fun Question.validateToDelete(user: User) {
        if (fromUserId != user.id) {
            throw DoriDoriException.of(DoriDoriExceptionType.NOT_ALLOWED_TO_ACCESS)
        }
    }

    fun Question.validateToDeny(user: User) {
        if (fromUserId != user.id) {
            throw DoriDoriException.of(DoriDoriExceptionType.NOT_ALLOWED_TO_ACCESS)
        }
    }

    fun Question.validateToAnswer(user: User) {
        if (toUserId != user.id) {
            throw DoriDoriException.of(DoriDoriExceptionType.NOT_ALLOWED_TO_ACCESS)
        }
    }
}