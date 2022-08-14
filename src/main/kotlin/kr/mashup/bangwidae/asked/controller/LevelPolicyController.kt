package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.ApiResponse
import kr.mashup.bangwidae.asked.controller.dto.LevelPolicyDto
import kr.mashup.bangwidae.asked.controller.dto.UserAchievementDto
import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.service.levelpolicy.LevelPolicyService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore

@Api(tags = ["레벨 정책 컨트롤러"])
@RestController
@RequestMapping("/api/v1/level-policy")
class LevelPolicyController(
    private val levelPolicyService: LevelPolicyService
) {
    @ApiOperation("레벨 정책 조회")
    @GetMapping("/{level}")
    fun getLevelPolicy(
        @PathVariable level: Int
    ): ApiResponse<LevelPolicyDto> {
        return levelPolicyService.getLevelPolicy(level)
            .let { ApiResponse.success(LevelPolicyDto.from(it)) }
    }

    @ApiOperation("사용자 레벨 조건 달성 현황 조회")
    @GetMapping("/achievement")
    fun getUserAchievement(
        @ApiIgnore @AuthenticationPrincipal authUser: User
    ): ApiResponse<UserAchievementDto> {
        return levelPolicyService.getUserAchievement(authUser)
            .let { ApiResponse.success(UserAchievementDto.from(it)) }
    }
}