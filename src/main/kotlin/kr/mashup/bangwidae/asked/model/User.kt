package kr.mashup.bangwidae.asked.model

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document("user")
data class User(
	val id: ObjectId? = null,
	val name: String? = "noname",
	val age: Long?,
	val userId: String,
	val password: String
)
