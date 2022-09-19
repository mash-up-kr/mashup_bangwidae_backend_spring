package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.model.document.Report
import kr.mashup.bangwidae.asked.model.document.ReportType
import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.repository.*
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class ReportService(
    private val reportRepository: ReportRepository,
    private val postRepository: PostRepository,
    private val answerRepository: AnswerRepository,
    private val questionRepository: QuestionRepository,
    private val commentRepository: CommentRepository
) {
    fun report(reporter: User, type: ReportType, targetId: ObjectId) {
        when (type) {
            ReportType.POST -> postRepository.deleteById(targetId)
            ReportType.ANSWER -> answerRepository.deleteById(targetId)
            ReportType.QUESTION -> questionRepository.deleteById(targetId)
            ReportType.COMMENT -> commentRepository.deleteById(targetId)
        }.let { reportRepository.save(Report(reporterUserId = reporter.id!!, type = type, targetId = targetId)) }
    }
}