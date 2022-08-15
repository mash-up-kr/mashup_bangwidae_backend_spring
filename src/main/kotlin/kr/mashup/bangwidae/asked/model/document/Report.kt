package kr.mashup.bangwidae.asked.model.document

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("report")
data class Report(
    @Id
    val id: ObjectId? = null,
    val reporterUserId: ObjectId,
    val type: ReportType,
    val targetId: ObjectId,
    val reason: String,
    val content: String?,

    @Version var version: Int? = null,
    @CreatedDate var createdAt: LocalDateTime? = null,
    @LastModifiedDate var updatedAt: LocalDateTime? = null,
)

enum class ReportType(val type: String) {
    POST("post"), QUESTION("question"), ANSWER("answer"), COMMENT("comment")
}