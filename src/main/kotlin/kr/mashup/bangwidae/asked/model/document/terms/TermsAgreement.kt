package kr.mashup.bangwidae.asked.model.document.terms

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("terms-agreement")
data class TermsAgreement (
    @Id
    val id: ObjectId? = null,
    val termsId: ObjectId,
    val userId: ObjectId,
    val agreementTime: LocalDateTime,

    @Version
    var version: Int? = null,
    @CreatedDate var createdAt: LocalDateTime? = null,
    @LastModifiedDate var updatedAt: LocalDateTime? = null,
)