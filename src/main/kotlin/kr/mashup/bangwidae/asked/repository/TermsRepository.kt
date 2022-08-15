package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.document.terms.Terms
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface TermsRepository: MongoRepository<Terms, ObjectId> {
    fun findAllByIdIn(ids: List<ObjectId>): List<Terms>
    fun findAllByDeletedFalse(): List<Terms>
}