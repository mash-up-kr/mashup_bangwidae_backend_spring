package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.post.CommentLike
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentLikeRepository : MongoRepository<CommentLike, ObjectId> {
}