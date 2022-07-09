package kr.mashup.bangwidae.asked.model.question

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("answer")
data class Answer(
    @Id
    val id: ObjectId? = null,
    val userId: ObjectId,
    val questionId: ObjectId,
    val content: String,

    val deleted: Boolean = false,
    @Version
    var version: Int? = null,
    @CreatedDate var createdAt: LocalDateTime? = null,
    @LastModifiedDate var updatedAt: LocalDateTime? = null,
) {
    fun updateContent(content: String): Answer {
        return this.copy(
            content = content,
        )
    }

    fun delete(): Answer {
        return this.copy(
            deleted = true,
        )
    }
}
