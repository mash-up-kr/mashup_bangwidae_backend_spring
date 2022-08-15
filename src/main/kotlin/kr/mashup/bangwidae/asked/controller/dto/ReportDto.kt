package kr.mashup.bangwidae.asked.controller.dto

import kr.mashup.bangwidae.asked.model.document.Report
import kr.mashup.bangwidae.asked.model.document.ReportType
import org.bson.types.ObjectId

data class ReportDto(
    val reporterUserId: ObjectId,
    val reason: String,
    val content: String?,
) {
    fun toEntity(type: String, targetId: ObjectId) = Report(
        reporterUserId = this.reporterUserId,
        targetId = targetId,
        reason = this.reason,
        content = this.content,
        type = ReportType.valueOf(type.uppercase())
    )
}