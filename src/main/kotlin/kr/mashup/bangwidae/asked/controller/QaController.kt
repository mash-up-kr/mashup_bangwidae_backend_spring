package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.ApiResponse
import kr.mashup.bangwidae.asked.controller.dto.JoinUserResponse
import kr.mashup.bangwidae.asked.controller.dto.QaJoinUserRequest
import kr.mashup.bangwidae.asked.service.QaService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["QA 컨트롤러"])
@RestController
@RequestMapping("/api/v1/qa")
class QaController(
    private val qaService: QaService
) {
    @ApiOperation("QA 용 회원가입(비밀번호 validation 과 이메일 인증 없이)")
    @PostMapping("/user/join")
    fun joinUser(
        @RequestBody qaJoinUserRequest: QaJoinUserRequest
    ): ApiResponse<JoinUserResponse> {
        return ApiResponse.success(qaService.joinUser(qaJoinUserRequest))
    }
}