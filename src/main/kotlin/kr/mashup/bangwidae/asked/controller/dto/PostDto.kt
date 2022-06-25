package kr.mashup.bangwidae.asked.controller.dto

import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.post.Post
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class PostDto(
	val id: ObjectId,
	val content: String = "",
	val longitude: Double,
	val latitude: Double,
	val representativeAddress: String?,
	val createdAt: LocalDateTime,
	val updatedAt: LocalDateTime
)

data class PostRequest(
	val content: String,
	val longitude: Double,
	val latitude: Double
)