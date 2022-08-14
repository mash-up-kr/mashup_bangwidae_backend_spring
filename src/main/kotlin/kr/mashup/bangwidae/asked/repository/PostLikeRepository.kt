package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.document.post.PostLike
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PostLikeRepository : MongoRepository<PostLike, ObjectId> {
    fun existsByPostIdAndUserId(postId: ObjectId, userId: ObjectId): Boolean
    fun findByPostIdAndUserId(postId: ObjectId, userId: ObjectId): PostLike?
    fun findAllByPostIdIn(postIdList: List<ObjectId>): List<PostLike>
}