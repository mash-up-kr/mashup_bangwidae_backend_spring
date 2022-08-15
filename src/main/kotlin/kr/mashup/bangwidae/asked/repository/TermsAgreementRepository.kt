package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.document.terms.TermsAgreement
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface TermsAgreementRepository: MongoRepository<TermsAgreement, ObjectId> {
}