package kr.mashup.bangwidae.asked.model.document.post

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("comment-like")
data class CommentLike(
    @Id
    val id: ObjectId? = null,
    val userId: ObjectId,
    val commentId: ObjectId
)