package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.document.Notification
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.MongoRepository

interface NotificationRepository : MongoRepository<Notification, ObjectId> {
    fun findByUserIdAndIdBeforeOrderByCreatedAtDesc(
        userId: ObjectId,
        lastId: ObjectId,
        pageRequest: PageRequest,
    ): List<Notification>

    fun countByUserIdAndReadFalse(userId: ObjectId): Long
}
