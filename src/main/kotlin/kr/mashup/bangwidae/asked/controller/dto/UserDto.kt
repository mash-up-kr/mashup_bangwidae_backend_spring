package kr.mashup.bangwidae.asked.controller.dto

import io.swagger.annotations.ApiModelProperty
import kr.mashup.bangwidae.asked.model.User
import org.bson.types.ObjectId

data class UserDto(
    @ApiModelProperty(value = "user id", example = "gardenlee")
    val id: String,
    @ApiModelProperty(value = "user nickname", example = "gardenlee")
    val nickname: String,
    @ApiModelProperty(value = "tags", example = "[MBTI]")
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

data class JoinUserRequest(
    @ApiModelProperty(value = "이메일", example = "doridori@gmail.com")
    val email: String,
    @ApiModelProperty(value = "로그인 비밀번호", example = "12345")
    val password: String
)

data class JoinUserResponse(
    @ApiModelProperty(value = "인증 토큰", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.~~~")
    val accessToken: String,
    @ApiModelProperty(value = "리프레시 토큰", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.~~~")
    val refreshToken: String
)

data class UpdateNicknameRequest (
    @ApiModelProperty(value = "닉네임", example = "도리를찾아서")
    val nickname: String
)

data class UpdateProfileRequest (
    @ApiModelProperty(value = "프로필 소개", example = "안녕하세요! 도리도리입니다.")
    val description: String,
    @ApiModelProperty(value = "관심사 리스트", example = "[MBTI, 넷플릭스]")
    val tags: List<String>,
)

data class AnsweredQuestionsDto (
    @ApiModelProperty(value = "헤더 텍스트", example = "새 질문이 3개나!")
    val headerText: String,
    val user: UserDto,
    val questions: List<QuestionDto>,
) {
    data class QuestionDto (
        @ApiModelProperty(value = "질문 내용", example = "#도리가 뭐야?")
        val content: String,
        @ApiModelProperty(value = "지역", example = "강남구")
        val representativeAddress: String,
        val user: UserDto,
        val answer: AnswerDto,
    )

    data class AnswerDto (
        @ApiModelProperty(value = "답변 내용", example = "안녕하세요! 도리도리입니다.")
        val content: String,
        @ApiModelProperty(value = "지역", example = "강남구")
        val representativeAddress: String,
        @ApiModelProperty(value = "좋아요 갯수", example = "4")
        val likeCount: Int,
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
                            likeCount = 1
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
                        ),
                        answer = AnswerDto(
                            content = "#니모 절친 물고기 임다",
                            representativeAddress = "강남구",
                            likeCount = 1
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
                        ),
                        answer = AnswerDto(
                            content = "#니모 절친 물고기 임다",
                            representativeAddress = "강남구",
                            likeCount = 1
                        )
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