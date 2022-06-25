package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.CertMail
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CertMailRepository: MongoRepository<CertMail, ObjectId> {
    fun findFirstByEmailAndOrderByMailSendTsDesc(email: String): CertMail?
}