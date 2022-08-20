package kr.mashup.bangwidae.asked.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.mashup.bangwidae.asked.controller.dto.ApiResponse
import kr.mashup.bangwidae.asked.controller.dto.CursorResult
import kr.mashup.bangwidae.asked.controller.dto.NotificationDto
import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.service.notification.NotificationService
import org.bson.types.ObjectId
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@Api(tags = ["알림 컨트롤러"])
@RestController
@RequestMapping("/api/v1/notifications")
class NotificationController(
    private val notificationService: NotificationService,
) {
    @ApiOperation("알림 페이징 조회")
    @GetMapping
    fun getMyNotifications(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @RequestParam size: Int,
        @RequestParam(required = false) lastId: ObjectId?,
    ): ApiResponse<CursorResult<NotificationDto>> {
        return notificationService.findByUser(
            user = user,
            lastId = lastId,
            size = size + 1,
        ).map { NotificationDto.from(it) }
            .let { CursorResult.from(it, size) }
            .let { ApiResponse.success(it) }
    }

    @ApiOperation("미확인 알림 개수 조회")
    @GetMapping("/unread-count")
    fun getMyUnreadNotificationsCount(
        @ApiIgnore @AuthenticationPrincipal user: User,
    ): ApiResponse<Long> {
        return notificationService.countUnreadByUser(
            user = user,
        ).let { ApiResponse.success(it) }
    }

    @ApiOperation("알림 읽음 처리")
    @PostMapping("/{notificationId}/read")
    fun readNotification(
        @ApiIgnore @AuthenticationPrincipal user: User,
        @PathVariable notificationId: ObjectId,
    ): ApiResponse<ObjectId> {
        return notificationService.read(
            notificationId = notificationId,
            user = user,
        ).let { ApiResponse.success(it.id!!) }
    }
}
