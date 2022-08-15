package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.ApiResponse
import kr.mashup.bangwidae.asked.controller.dto.TermsDto
import kr.mashup.bangwidae.asked.service.TermsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["약관 컨트롤러"])
@RestController
@RequestMapping("/api/v1/terms")
class TermsController(
    private val termsService: TermsService,
) {

    @ApiOperation("약관 조회")
    @GetMapping
    fun getTerms(): ApiResponse<List<TermsDto>>{
        return termsService.getTerms()
            .map { TermsDto.from(it) }
            .let { ApiResponse.success(it) }
    }
}