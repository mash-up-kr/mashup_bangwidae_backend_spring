package kr.mashup.bangwidae.asked.service.post

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.model.document.post.Post

interface WithPostAuthorityValidator {
    fun Post.validateToUpdate(user: User) {
        if (userId != user.id) {
            throw DoriDoriException.of(DoriDoriExceptionType.POST_NOT_ALLOWED_FOR_USER)
        }
    }

    fun Post.validateToDelete(user: User) {
        if (userId != user.id) {
            throw DoriDoriException.of(DoriDoriExceptionType.POST_NOT_ALLOWED_FOR_USER)
        }
    }
}