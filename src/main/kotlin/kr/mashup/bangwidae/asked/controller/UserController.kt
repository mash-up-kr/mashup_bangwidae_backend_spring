package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.*
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.service.HeaderTextType
import kr.mashup.bangwidae.asked.service.UserService
import kr.mashup.bangwidae.asked.service.WardService
import kr.mashup.bangwidae.asked.service.post.PostService
import kr.mashup.bangwidae.asked.service.question.QuestionService
import org.bson.types.ObjectId
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import springfox.documentation.annotations.ApiIgnore

@Api(tags = ["유저 컨트롤러"])
@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService,
    private val questionService: QuestionService,
    private val wardService: WardService,
    private val postService: PostService
) {

    @ApiOperation("ID/PW 회원가입")
    @PostMapping("/join")
    fun joinUser(
        @RequestBody joinUserRequest: JoinUserRequest
    ): ApiResponse<JoinUserResponse> {
        return ApiResponse.success(userService.joinUser(joinUserRequest))
    }

    @ApiOperation("유저 정보")
    @GetMapping("/{userId}/info")
    fun getUserInfo(
        @PathVariable userId: ObjectId
    ): ApiResponse<UserInfoDto> {
        return ApiResponse.success(userService.getUserInfo(userId))
    }

    @ApiOperation("유저 링크 공유")
    @GetMapping("/{userId}/link-share")
    fun getUserLinkShareInfo(
        @ApiIgnore @AuthenticationPrincipal authUser: User?,
        @PathVariable userId: ObjectId
    ): ApiResponse<UserLinkShareInfoDto> {
        val user = userService.findById(userId)
        val questions = questionService.findAnswerCompleteByToUser(
            authUser = authUser,
            toUserID = userId,
            lastId = null,
            size = 2,
        )
        val representativeWard = wardService.getMyRepresentativeWard(user)
        return ApiResponse.success(UserLinkShareInfoDto.from(user, questions, representativeWard))
    }

    @ApiOperation("닉네임 설정")
    @PostMapping("/nickname")
    fun createNickname(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @RequestBody updateNicknameRequest: UpdateNicknameRequest
    ): ApiResponse<Boolean> {
        return ApiResponse.success(userService.updateNickname(user, updateNicknameRequest.nickname))
    }

    @ApiOperation("프로필 설정")
    @PostMapping("/profile")
    fun createProfile(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @RequestBody updateProfileRequest: UpdateProfileRequest
    ): ApiResponse<Boolean> {
        return ApiResponse.success(
            userService.updateProfile(
                user,
                updateProfileRequest.description,
                updateProfileRequest.tags,
                updateProfileRequest.representativeWardId
            )
        )
    }

    @ApiOperation("프로필 이미지 업로드")
    @PostMapping("/profile/image")
    fun profileImageUpload(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @RequestParam image: MultipartFile
    ): ApiResponse<String> {
        return ApiResponse.success(userService.updateProfileImage(user, image))
    }

    @ApiOperation("프로필 이미지 기본값으로 변경")
    @PostMapping("/profile/image/default")
    fun setDefaultProfileImage(
        @ApiIgnore @AuthenticationPrincipal user: User
    ): ApiResponse<Boolean> {
        return ApiResponse.success(userService.updateToDefaultProfileImage(user))
    }

    @ApiOperation("내 정보")
    @GetMapping("/me")
    fun getMyInfo(
        @ApiIgnore @AuthenticationPrincipal user: User
    ): ApiResponse<UserInfoDto> {
        val representativeWard = wardService.getMyRepresentativeWard(user)
        return ApiResponse.success(UserInfoDto.from(user, representativeWard))
    }

    @ApiOperation("헤더 문구")
    @GetMapping("/header-text")
    fun getHeaderText(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @RequestParam type: HeaderTextType,
    ): ApiResponse<String> {
        return ApiResponse.success(userService.getUserHeaderText(user, type))
    }

    @ApiOperation("답변완료(본인)")
    @GetMapping("/answered-questions")
    fun getMyAnsweredQuestions(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @RequestParam size: Int,
        @RequestParam(required = false) lastId: ObjectId?,
    ): ApiResponse<AnsweredQuestionsDto> {
        return questionService.findAnswerCompleteByToUser(
            authUser = user,
            toUser = user,
            lastId = lastId,
            size = size + 1,
        ).let {
            ApiResponse.success(
                AnsweredQuestionsDto.from(
                    questions = it,
                    requestedSize = size,
                )
            )
        }
    }

    @ApiOperation("받은 질문(본인)")
    @GetMapping("/received-questions")
    fun getMyReceivedQuestions(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @RequestParam size: Int,
        @RequestParam(required = false) lastId: ObjectId?,
    ): ApiResponse<ReceivedQuestionsDto> {
        return questionService.findAnswerWaitingByToUser(
            toUser = user,
            lastId = lastId,
            size = size + 1,
        ).let {
            ApiResponse.success(
                ReceivedQuestionsDto.from(
                    questions = it,
                    requestedSize = size,
                )
            )
        }
    }

    @ApiOperation("한 질문(본인)")
    @GetMapping("/asked-questions")
    fun getMyAskedQuestions(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @RequestParam size: Int,
        @RequestParam(required = false) lastId: ObjectId?,
    ): ApiResponse<AskedQuestionsDto> {
        return questionService.findByFromUser(
            fromUser = user,
            lastId = lastId,
            size = size + 1,
        ).let {
            ApiResponse.success(
                AskedQuestionsDto.from(
                    questions = it,
                    requestedSize = size,
                )
            )
        }
    }

    @ApiOperation("한 질문(본인, Post)")
    @GetMapping("/asked-posts")
    fun getMyAskedPosts(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @RequestParam size: Int,
        @RequestParam(required = false) lastId: ObjectId?,
    ): ApiResponse<CursorResult<PostDto>> {
        return postService.findByFromUser(user = user, lastId = lastId, size = size + 1)
            .let { postList ->
                ApiResponse.success(
                    CursorResult.from(
                        values = postList.map { PostDto.from(it) },
                        requestedSize = size
                    )
                )
            }
    }

    @ApiOperation("유저 설정 정보")
    @GetMapping("/settings")
    fun getUserSettings(
        @ApiIgnore @AuthenticationPrincipal user: User,
    ): ApiResponse<UserSettingsDto> {
        return UserSettingsDto.from(user)
            .let { ApiResponse.success(it) }
    }

    @ApiOperation("유저 설정 변경")
    @PutMapping("/settings")
    fun editUserSettings(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @RequestBody editUserSettingsRequest: EditUserSettingsRequest,
    ): ApiResponse<UserSettingsDto> {
        return userService.editUserSettings(user, editUserSettingsRequest)
            .let { UserSettingsDto.from(it) }
            .let { ApiResponse.success(it) }
    }

    @ApiOperation("유저 탈퇴")
    @DeleteMapping
    fun leaveUser(
        @ApiIgnore @AuthenticationPrincipal user: User
    ): ApiResponse<Boolean> {
        return userService.delete(user)
            .let { ApiResponse.success(true) }
    }

    @ApiOperation("유저 차단")
    @PostMapping("block/{toUserId}")
    fun blockUser(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @PathVariable toUserId: String
    ): ApiResponse<Boolean> {
        if (user.id!! == ObjectId(toUserId)) {
            throw DoriDoriException.of(DoriDoriExceptionType.USER_CANNOT_BLOCK_ME)
        }
        return userService.blockUser(user, ObjectId(toUserId))
            .let { ApiResponse.success(true) }
    }
}