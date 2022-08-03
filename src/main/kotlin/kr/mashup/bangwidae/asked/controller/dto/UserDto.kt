package kr.mashup.bangwidae.asked.controller.dto

import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.User.Companion.DEFAULT_PROFILE_IMAGE_URL
import kr.mashup.bangwidae.asked.service.question.QuestionDomain

data class UserInfoDto(
    val userId: String,
    val nickname: String,
    val profileDescription: String,
    val tags: List<String>,
) {
    companion object {
        fun from(user: User): UserInfoDto {
            return UserInfoDto(
                userId = user.id!!.toHexString(),
                nickname = user.nickname!!,
                profileDescription = user.description!!,
                tags = user.tags,
            )
        }
    }
}

data class UserLinkShareInfoDto(
    val user: UserInfoDto,
    val representativeWardName: String,
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
                profileImageUrl = user.profileImageUrl ?: DEFAULT_PROFILE_IMAGE_URL,
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
                questionId = question.id,
                questionContent = question.content,
                answerContent = question.answer!!.content,
            )
        }
    }

    companion object {
        fun from(user: User, questions: List<QuestionDomain>) = UserLinkShareInfoDto(
            user = UserInfoDto.from(user),
            representativeWardName = "todo - 우리집",
            questions = questions.map { QuestionAndAnswerDto.from(it) },
        )
    }
}

data class JoinUserRequest(
    val email: String,
    val password: String
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
)
