package kr.mashup.bangwidae.asked.controller.dto

import io.swagger.annotations.ApiModelProperty
import kr.mashup.bangwidae.asked.model.User
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class UserDto(
    val id: String,
    val nickname: String,
    val tags: List<String>,
) {
    companion object {
        fun from(user: User): UserDto {
            return UserDto(
                id = user.id!!.toHexString(),
                nickname = user.nickname!!,
                tags = user.tags,
            )
        }
    }
}

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

data class JoinUserRequest(
    val email: String,
    val password: String
)

data class JoinUserResponse(
    val accessToken: String,
    val refreshToken: String
)

data class UpdateNicknameRequest (
    val nickname: String
)

data class UpdateProfileRequest (
    val description: String,
    val tags: List<String>,
)

data class AnsweredQuestionsDto (
    val headerText: String,
    val user: UserDto,
    val questions: List<QuestionDto>,
) {
    data class QuestionDto (
        val content: String,
        val representativeAddress: String,
        val user: UserDto,
        val answer: AnswerDto,
        val createdAt: LocalDateTime,
    )

    data class AnswerDto (
        val content: String,
        val representativeAddress: String,
        val likeCount: Int,
        val createdAt: LocalDateTime,
    )

    companion object {
        fun createMock(): AnsweredQuestionsDto {
            return AnsweredQuestionsDto(
                headerText = "새로운 질문이 3개 도착했어요!",
                user = UserDto(
                    id = ObjectId("62c9797528889852507cec07").toHexString(),
                    nickname = "도리도리도링",
                    tags = listOf(
                        "MBTI", "디즈니"
                    ),
                ),
                questions = listOf(
                    QuestionDto(
                        content = "#도리 를 찾아서가 뭐에요?",
                        representativeAddress = "강남구",
                        user = UserDto(
                            id = ObjectId("62c9797528889852507cec07").toHexString(),
                            nickname = "감자도리도리",
                            tags = listOf(
                                "MBTI", "디즈니"
                            ),
                        ),
                        answer = AnswerDto(
                            content = "#니모 절친 물고기 임다",
                            representativeAddress = "강남구",
                            likeCount = 1,
                            createdAt = LocalDateTime.now()
                        ),
                        createdAt = LocalDateTime.now().minusHours(1)
                    ),
                    QuestionDto(
                        content = "#도리 를 찾아서가 뭐에요?",
                        representativeAddress = "강남구",
                        user = UserDto(
                            id = ObjectId("62c9797528889852507cec07").toHexString(),
                            nickname = "감자도리도리",
                            tags = listOf(
                                "MBTI", "디즈니"
                            ),
                        ),
                        answer = AnswerDto(
                            content = "#니모 절친 물고기 임다",
                            representativeAddress = "강남구",
                            likeCount = 1,
                            createdAt = LocalDateTime.now()
                        ),
                        createdAt = LocalDateTime.now().minusHours(2),
                    ),
                    QuestionDto(
                        content = "#도리 를 찾아서가 뭐에요?",
                        representativeAddress = "강남구",
                        user = UserDto(
                            id = ObjectId("62c9797528889852507cec07").toHexString(),
                            nickname = "감자도리도리",
                            tags = listOf(
                                "MBTI", "디즈니"
                            ),
                        ),
                        answer = AnswerDto(
                            content = "#니모 절친 물고기 임다",
                            representativeAddress = "강남구",
                            likeCount = 1,
                            createdAt = LocalDateTime.now().minusMinutes(40)
                        ),
                        createdAt = LocalDateTime.now().minusHours(2),
                    )
                )
            )
        }
    }
}

data class ReceivedQuestionsDto(
    val questions: List<QuestionDto>
) {
    data class QuestionDto (
        val content: String,
        val representativeAddress: String,
        val user: UserDto,
    )

    companion object{
        fun createMock(): ReceivedQuestionsDto {
            return ReceivedQuestionsDto(
                listOf(
                    QuestionDto(
                        content = "#도리 를 찾아서가 뭐에요?",
                        representativeAddress = "강남구",
                        user = UserDto(
                            id = ObjectId("62c9797528889852507cec07").toHexString(),
                            nickname = "감자도리도리",
                            tags = listOf(
                                "MBTI", "디즈니"
                            ),
                        )
                    ),
                    QuestionDto(
                        content = "#도리 를 찾아서가 뭐에요?",
                        representativeAddress = "강남구",
                        user = UserDto(
                            id = ObjectId("62c9797528889852507cec07").toHexString(),
                            nickname = "감자도리도리",
                            tags = listOf(
                                "MBTI", "디즈니"
                            ),
                        )
                    ),
                    QuestionDto(
                        content = "#도리 를 찾아서가 뭐에요?",
                        representativeAddress = "강남구",
                        user = UserDto(
                            id = ObjectId("62c9797528889852507cec07").toHexString(),
                            nickname = "감자도리도리",
                            tags = listOf(
                                "MBTI", "디즈니"
                            ),
                        )
                    )
                )
            )
        }
    }
}