package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.question.Answer
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AnswerRepository : MongoRepository<Answer, ObjectId> {
    fun countByQuestionIdAndDeletedFalse(questionId: ObjectId): Long
}
