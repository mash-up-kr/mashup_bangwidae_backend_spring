package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.model.document.Report
import kr.mashup.bangwidae.asked.repository.ReportRepository
import org.springframework.stereotype.Service

@Service
class ReportService(
    private val reportRepository: ReportRepository
) {
    fun report(report: Report) {
        reportRepository.save(report)
    }
}