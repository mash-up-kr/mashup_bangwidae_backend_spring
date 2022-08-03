package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.question.Question
import kr.mashup.bangwidae.asked.model.question.QuestionStatus
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface QuestionRepository : MongoRepository<Question, ObjectId> {
    fun countAllByFromUserIdAndDeletedFalse(fromUserId: ObjectId): Int

    fun findByIdAndDeletedFalse(id: ObjectId): Question?

    fun findByToUserIdAndStatusAndIdBeforeAndDeletedFalseOrderByCreatedAtDesc(
        toUserId: ObjectId,
        status: QuestionStatus,
        lastId: ObjectId,
        pageRequest: PageRequest,
    ): List<Question>

    fun countByToUserIdAndStatusAndDeletedFalse(
        toUserId: ObjectId,
        status: QuestionStatus,
    ): Long

    fun findByFromUserIdAndIdBeforeAndDeletedFalseOrderByCreatedAtDesc(
        fromUserId: ObjectId,
        lastId: ObjectId,
        pageRequest: PageRequest,
    ): List<Question>
}
