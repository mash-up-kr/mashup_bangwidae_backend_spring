package kr.mashup.bangwidae.asked.controller.dto

import kr.mashup.bangwidae.asked.model.document.Report
import kr.mashup.bangwidae.asked.model.document.ReportType
import kr.mashup.bangwidae.asked.model.document.User
import org.bson.types.ObjectId

data class ReportDto(
    val reason: String,
    val content: String?,
) {
    fun toEntity(reporter: User, type: String, targetId: ObjectId) = Report(
        reporterUserId = reporter.id!!,
        targetId = targetId,
        reason = this.reason,
        content = this.content,
        type = ReportType.valueOf(type.uppercase())
    )
}