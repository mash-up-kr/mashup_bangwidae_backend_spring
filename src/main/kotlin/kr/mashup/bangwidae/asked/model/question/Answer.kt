package kr.mashup.bangwidae.asked.model.question

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("answer")
data class Answer(
    val id: ObjectId? = null,
    val userId: ObjectId,
    val questionId: ObjectId,
    val content: String,

    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @LastModifiedDate
    val updatedAt: LocalDateTime = LocalDateTime.now()
)