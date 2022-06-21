package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.LoginRequest
import kr.mashup.bangwidae.asked.controller.dto.LoginResponse
import org.springframework.web.bind.annotation.*

@Api(tags = ["인증 컨트롤러 (로그인)"])
@RestController
@RequestMapping("/api/v1/auth")
class AuthController {

    @ApiOperation("로그인")
    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest
    ): LoginResponse {
        return LoginResponse(token = "example")
    }

}