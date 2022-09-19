package kr.mashup.bangwidae.asked.model.document

import kr.mashup.bangwidae.asked.service.notification.NotificationType
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("notification")
data class Notification(
    @Id
    val id: ObjectId? = null,
    val userId: ObjectId,
    val type: NotificationType,
    val title: String,
    val content: String,
    val read: Boolean = false,
    val urlScheme: String,
    @Version var version: Int? = null,
    @CreatedDate var createdAt: LocalDateTime? = null,
    @LastModifiedDate var updatedAt: LocalDateTime? = null
) {
    fun read(): Notification {
        return this.copy(read = true)
    }
}
