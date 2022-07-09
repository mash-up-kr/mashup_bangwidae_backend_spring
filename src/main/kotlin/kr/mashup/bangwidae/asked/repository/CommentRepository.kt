package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.post.Comment
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository: MongoRepository<Comment, ObjectId> {
    fun findByIdAndDeletedFalse(id: ObjectId): Comment?
}