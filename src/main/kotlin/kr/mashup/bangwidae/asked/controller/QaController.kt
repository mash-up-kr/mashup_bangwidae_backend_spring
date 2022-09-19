package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.ApiResponse
import kr.mashup.bangwidae.asked.controller.dto.JoinUserResponse
import kr.mashup.bangwidae.asked.controller.dto.QaJoinUserRequest
import kr.mashup.bangwidae.asked.service.QaService
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.*

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

    @ApiOperation("임시용 타입 판별기")
    @GetMapping("/type/{id}")
    fun joinUser(
        @PathVariable id : String
    ): ApiResponse<String> {
        return ApiResponse.success(qaService.findType(ObjectId(id)))
    }
}