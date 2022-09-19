package kr.mashup.bangwidae.asked.model.document.question

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("answer-like")
data class AnswerLike(
    @Id
    val id: ObjectId? = null,
    val userId: ObjectId,
    val answerId: ObjectId
)