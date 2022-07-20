package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.post.PostLike
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PostLikeRepository : MongoRepository<PostLike, ObjectId> {
}