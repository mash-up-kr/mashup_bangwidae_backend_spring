package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.question.AnswerLike
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AnswerLikeRepository : MongoRepository<AnswerLike, ObjectId> {
}