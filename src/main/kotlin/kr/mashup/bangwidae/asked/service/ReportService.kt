package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.model.document.Report
import kr.mashup.bangwidae.asked.model.document.ReportType
import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.repository.*
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference
import javax.annotation.PostConstruct

@Service
class ReportService(
    private val reportRepository: ReportRepository,
) {
    private val reportMap: AtomicReference<ConcurrentHashMap<ObjectId, List<Report>>> = AtomicReference()

    @PostConstruct
    fun init() {
        setReportMap()
    }

    fun setReportMap() {
        ConcurrentHashMap<ObjectId, List<Report>>().apply {
            putAll(reportRepository.findAll().groupBy { it.reporterUserId })
        }.let { reportMap.set(it) }
    }

    fun report(reporter: User, type: ReportType, targetId: ObjectId) {
        reportRepository.save(Report(reporterUserId = reporter.id!!, type = type, targetId = targetId))
            .also { setReportMap() }
    }

    fun getReportMap() = reportMap
}