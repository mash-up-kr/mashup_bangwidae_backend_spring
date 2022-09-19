package kr.mashup.bangwidae.asked.model.domain

import kr.mashup.bangwidae.asked.model.document.User
import org.bson.types.ObjectId

data class WriterUserDomain(
    val id: ObjectId,
    val nickname: String,
    val tags: List<String>,
    val profileImageUrl: String?,
    val level: Int
) {
    companion object {
        fun from(user: User) = WriterUserDomain(
            id = user.id!!,
            nickname = user.nickname!!,
            tags = user.tags,
            profileImageUrl = user.userProfileImageUrl,
            level = user.level
        )

        fun of(user: User, anonymous: Boolean) =
            when {
                user.deleted -> from(User.deletedUser())
                anonymous -> from(user.getAnonymousUser())
                else -> from(user)
            }
    }
}
