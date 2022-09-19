package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.ApiResponse
import kr.mashup.bangwidae.asked.model.document.ReportType
import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.service.ReportService
import org.bson.types.ObjectId
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore

@Api(tags = ["신고 컨트롤러"])
@RestController
@RequestMapping("/api/v1/report")
class ReportController(
    private val reportService: ReportService
) {
    @ApiOperation("신고")
    @PostMapping("/{type}/{targetId}")
    fun report(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @PathVariable targetId: ObjectId,
        @PathVariable type: String
    ): ApiResponse<Boolean> {
        return reportService.report(user, ReportType.valueOf(type.uppercase()), targetId)
            .let { ApiResponse.success(true) }
    }
}