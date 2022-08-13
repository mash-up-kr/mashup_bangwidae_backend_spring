package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.Notification
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface NotificationRepository: MongoRepository<Notification, ObjectId>