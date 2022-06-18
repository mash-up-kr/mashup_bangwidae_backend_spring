package kr.mashup.bangwidae.asked.model.question

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("question")
data class Question (
	val id: ObjectId? = null,
	val content: String,

	@CreatedDate
	val createdAt: LocalDateTime = LocalDateTime.MIN,
	@LastModifiedDate
	val updatedAt: LocalDateTime = LocalDateTime.MIN
)