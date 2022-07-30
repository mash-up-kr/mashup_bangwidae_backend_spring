package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.post.CommentLike
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentLikeRepository : MongoRepository<CommentLike, ObjectId> {
    fun existsByCommentIdAndUserId(commentId: ObjectId, userId: ObjectId): Boolean
    fun findByCommentIdAndUserId(commentId: ObjectId, userId: ObjectId): CommentLike?
    fun findAllByCommentIdIn(commentIdList: List<ObjectId>): List<CommentLike>
}