package kr.mashup.bangwidae.asked.service.question

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.question.Answer

interface WithAnswerAuthorityValidator {
    fun Answer.validateToUpdate(user: User) {
        if (userId != user.id) {
            throw DoriDoriException.of(DoriDoriExceptionType.NOT_ALLOWED_TO_ACCESS)
        }
    }

    fun Answer.validateToDelete(user: User) {
        if (userId != user.id) {
            throw DoriDoriException.of(DoriDoriExceptionType.NOT_ALLOWED_TO_ACCESS)
        }
    }
}