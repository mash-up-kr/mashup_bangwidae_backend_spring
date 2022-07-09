package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, ObjectId> {
    fun findByEmail(email: String): User?
    fun findByNickname(nickname: String): User?
}