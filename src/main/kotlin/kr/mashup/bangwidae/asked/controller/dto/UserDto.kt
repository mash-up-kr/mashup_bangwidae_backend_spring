package kr.mashup.bangwidae.asked.controller.dto

import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.model.document.Ward
import kr.mashup.bangwidae.asked.model.domain.QuestionDomain
import org.bson.types.ObjectId

data class UserInfoDto(
    val userId: String,
    val nickname: String?,
    val profileDescription: String?,
    val tags: List<String>,
    val representativeWard: WardDto?,
    val level: Int,
    val profileImageUrl: String?
) {
    companion object {
        fun from(user: User, representativeWard: Ward?): UserInfoDto {
            return UserInfoDto(
                userId = user.id!!.toHexString(),
                nickname = user.nickname,
                profileDescription = user.description,
                tags = user.tags,
                representativeWard = representativeWard?.let { WardDto.from(it) },
                profileImageUrl = user.userProfileImageUrl,
                level = user.level
            )
        }
    }
}

data class UserLinkShareInfoDto(
    val user: UserInfoDto,
    val representativeWard: WardDto?,
    val questions: List<QuestionAndAnswerDto>,
) {
    data class UserInfoDto(
        val id: String,
        val nickname: String,
        val profileDescription: String,
        val tags: List<String>,
        val profileImageUrl: String,
        val level: Int,
    ) {
        companion object {
            fun from(user: User) = UserInfoDto(
                id = user.id!!.toHexString(),
                nickname = user.nickname!!,
                profileDescription = user.description ?: "",
                tags = user.tags,
                profileImageUrl = user.userProfileImageUrl,
                level = user.level,
            )
        }
    }

    data class QuestionAndAnswerDto(
        val questionId: String,
        val questionContent: String,
        val answerContent: String,
    ) {
        companion object {
            fun from(question: QuestionDomain) = QuestionAndAnswerDto(
                questionId = question.id.toHexString(),
                questionContent = question.content,
                answerContent = question.answer!!.content,
            )
        }
    }

    companion object {
        fun from(user: User, questions: List<QuestionDomain>, representativeWard: Ward?) = UserLinkShareInfoDto(
            user = UserInfoDto.from(user),
            representativeWard = representativeWard?.let { WardDto.from(it) },
            questions = questions.map { QuestionAndAnswerDto.from(it) },
        )
    }
}

data class JoinUserRequest(
    val email: String,
    val password: String,
    val termsIds: List<ObjectId>,
)

data class JoinUserResponse(
    val accessToken: String,
    val refreshToken: String
)

data class UpdateNicknameRequest(
    val nickname: String
)

data class UpdateProfileRequest(
    val description: String,
    val tags: List<String>,
    val representativeWardId: ObjectId?
)

data class UserSettingsDto(
    val userId: String,
    val notification: Boolean,
    val nightNotification: Boolean,
    val locationInfo: Boolean,
) {
    companion object {
        fun from(user: User): UserSettingsDto {
            return UserSettingsDto(
                userId = user.id!!.toHexString(),
                notification = user.settings.notification,
                nightNotification = user.settings.nightNotification,
                locationInfo = user.settings.locationInfo,
            )
        }
    }
}

data class EditUserSettingsRequest(
    val notification: Boolean,
    val nightNotification: Boolean,
    val locationInfo: Boolean,
)