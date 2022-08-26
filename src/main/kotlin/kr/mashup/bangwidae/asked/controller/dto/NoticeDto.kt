package kr.mashup.bangwidae.asked.controller.dto

import kr.mashup.bangwidae.asked.model.Notice
import java.time.LocalDateTime

data class NoticeDto(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(notice: Notice): NoticeDto {
            return NoticeDto(
                id = notice.id!!.toHexString(),
                title = notice.title,
                content = notice.content,
                createdAt = notice.createdAt!!
            )
        }
    }
}

data class NoticeRegisterDto(
    val title: String,
    val content: String
)