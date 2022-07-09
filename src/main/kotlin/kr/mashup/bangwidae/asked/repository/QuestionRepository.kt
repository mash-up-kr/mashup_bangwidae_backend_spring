package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.question.Question
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface QuestionRepository : MongoRepository<Question, ObjectId> {
    fun findByIdAndDeletedFalse(id: ObjectId): Question?
}
