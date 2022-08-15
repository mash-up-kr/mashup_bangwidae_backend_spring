package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.document.Report
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportRepository : MongoRepository<Report, ObjectId> {
}