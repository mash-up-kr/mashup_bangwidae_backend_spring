package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.*
import kr.mashup.bangwidae.asked.model.Notice
import kr.mashup.bangwidae.asked.service.NoticeService
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.*

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

    @ApiOperation("공지사항 등록")
    @PostMapping
    fun registerNotice(
        @RequestBody noticeRegisterRequest : NoticeRegisterDto
    ): ApiResponse<NoticeDto> {
        return noticeService.register(
            Notice(
                title = noticeRegisterRequest.title,
                content = noticeRegisterRequest.content
            )
        ).let { ApiResponse.success(NoticeDto.from(it)) }
    }
}