package kr.mashup.bangwidae.asked.model

import kr.mashup.bangwidae.asked.service.alarm.NotificationType
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
    /**
     * 고민.. Url Scheme 저장이 필요할까?
     * 그냥 Type 만 저장해 놓고 Notification 조회 결과 반환 전에
     * Type 에 맞는 규칙으로 애플리케이션에서 적용해서 Url Scheme 을 생성하는 건 어떤가요..?
     * 이유는 Url Scheme 규칙이 클라이언트에서 변경됐을 때 DB 마이그레이션 보다 애플리케이션에서 규칙만 변경하면 편할 거 같아서!
     */
//    val urlScheme: String,
    @Version var version: Int? = null,
    @CreatedDate var createdAt: LocalDateTime? = null,
    @LastModifiedDate var updatedAt: LocalDateTime? = null
)


