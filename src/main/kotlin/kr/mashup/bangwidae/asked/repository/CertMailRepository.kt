package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.document.CertMail
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CertMailRepository: MongoRepository<CertMail, ObjectId> {
    fun findFirstByEmailOrderByMailSendTsDesc(email: String): CertMail?
}