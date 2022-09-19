package kr.mashup.bangwidae.asked.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("blacklist")
data class BlackList (
    @Id
    val id: ObjectId? = null,
    val fromUserId: ObjectId,
    val toUserId: ObjectId
)