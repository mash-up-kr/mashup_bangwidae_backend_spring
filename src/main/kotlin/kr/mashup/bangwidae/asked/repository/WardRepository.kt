package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.Ward
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface WardRepository : MongoRepository<Ward, ObjectId> {
    fun countAllByUserId(userId: ObjectId): Int
    fun findAllByUserIdAndExpiredAtAfter(userId: ObjectId, expiredAt: LocalDateTime): List<Ward>
    fun findWardByUserIdAndExpiredAtAfterAndIsRepresentativeTrue(userId: ObjectId, expiredAt: LocalDateTime): Ward?
    fun findWardByIdAndExpiredAtAfter(wardId: ObjectId, expiredAt: LocalDateTime): Ward?
}