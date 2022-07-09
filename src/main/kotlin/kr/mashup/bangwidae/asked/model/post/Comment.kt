package kr.mashup.bangwidae.asked.model.post

import kr.mashup.bangwidae.asked.model.Region
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("comment")
data class Comment(
    val id: ObjectId? = null,
    val userId: ObjectId,
    val postId: ObjectId,
    val content: String,
    val region: Region? = null,

    val deleted: Boolean = false,
    @Version
    var version: Int? = null,
    @CreatedDate var createdAt: LocalDateTime? = null,
    @LastModifiedDate var updatedAt: LocalDateTime? = null
) {
    fun updateContent(content: String): Comment {
        return this.copy(
            content = content,
        )
    }

    fun delete(): Comment {
        return this.copy(
            deleted = true,
        )
    }
}