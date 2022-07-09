package kr.mashup.bangwidae.asked.service.post

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.post.Comment

interface WithCommentAuthorityValidator {
    fun Comment.validateToUpdate(user: User) {
        if (userId != user.id) {
            throw DoriDoriException.of(DoriDoriExceptionType.POST_NOT_ALLOWED_FOR_USER)
        }
    }

    fun Comment.validateToDelete(user: User) {
        if (userId != user.id) {
            throw DoriDoriException.of(DoriDoriExceptionType.POST_NOT_ALLOWED_FOR_USER)
        }
    }
}