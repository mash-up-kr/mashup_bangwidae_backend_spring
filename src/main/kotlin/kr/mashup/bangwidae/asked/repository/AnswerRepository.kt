package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.question.Answer
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AnswerRepository : MongoRepository<Answer, ObjectId> {
    fun countAllByUserIdAndDeletedFalse(userId: ObjectId): Int
    fun findByIdAndDeletedFalse(id: ObjectId): Answer?
    fun existsByIdAndDeletedFalse(id: ObjectId): Boolean
    fun countByQuestionIdAndDeletedFalse(questionId: ObjectId): Long
    fun findByQuestionIdAndDeletedFalse(id: ObjectId): List<Answer>
    fun findByQuestionIdInAndDeletedFalseOrderByCreatedAtDesc(ids: List<ObjectId>): List<Answer>
}
