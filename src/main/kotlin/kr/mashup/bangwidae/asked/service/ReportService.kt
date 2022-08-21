package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.model.document.Report
import kr.mashup.bangwidae.asked.model.document.ReportType
import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.repository.ReportRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class ReportService(
    private val reportRepository: ReportRepository
) {
    fun report(reporter: User, type: ReportType, targetId: ObjectId) {
        reportRepository.save(Report(reporterUserId = reporter.id!!, type = type, targetId = targetId))
    }
}