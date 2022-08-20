package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.ApiResponse
import kr.mashup.bangwidae.asked.controller.dto.CursorResult
import kr.mashup.bangwidae.asked.controller.dto.NoticeDto
import kr.mashup.bangwidae.asked.service.NoticeService
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["공지 컨트롤러"])
@RestController
@RequestMapping("/api/v1/notice")
class NoticeController(
    private val noticeService: NoticeService,
) {
    @ApiOperation("공지사항 리스트")
    @GetMapping
    fun getNoticeList(
        @RequestParam size: Int,
        @RequestParam(required = false) lastId: ObjectId?
    ): ApiResponse<CursorResult<NoticeDto>> {
        return noticeService.getNoticeList(lastId, size + 1)
            .map { NoticeDto.from(it) }
            .let { ApiResponse.success(CursorResult.from(values = it, requestedSize = size)) }
    }
}