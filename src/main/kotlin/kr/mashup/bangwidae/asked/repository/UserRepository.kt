package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.document.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, ObjectId> {
    @Query("{'email' : ?0, 'deleted' : false}")
    fun findByEmail(email: String): User?
    fun findAllByIdIn(idList: Collection<ObjectId>): List<User>
    fun findByNickname(nickname: String): User?
}