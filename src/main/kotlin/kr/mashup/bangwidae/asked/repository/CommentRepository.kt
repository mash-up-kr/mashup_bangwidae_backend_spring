package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.post.Comment
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : MongoRepository<Comment, ObjectId> {
    fun countAllByUserIdAndDeletedFalse(userId: ObjectId): Int
    fun findByIdAndDeletedFalse(id: ObjectId): Comment?
    fun findAllByPostIdIn(postIdList: List<ObjectId>): List<Comment>
    fun existsByIdAndDeletedFalse(id: ObjectId): Boolean
    fun findByPostIdAndIdBeforeAndDeletedFalseOrderByIdDesc(
        postId: ObjectId,
        lastId: ObjectId,
        pageRequest: PageRequest
    ): List<Comment>
}