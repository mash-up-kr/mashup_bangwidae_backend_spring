package kr.mashup.bangwidae.asked.model.document

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("cert-mail")
data class CertMail(
    @Id
    val id: ObjectId? = null,
    val email: String,
    val certificationNumber: String,
    var isCertificated: Boolean = false,
    var certificationTs: LocalDateTime? = null,
    val mailSendTs: LocalDateTime = LocalDateTime.now(),
    val expiredTs: LocalDateTime // 유효기간 검사 필요할 때 develop
) {
    fun isCertifiedBy(number: String): Boolean {
        return this.certificationNumber == number
    }

    fun isExpired(): Boolean {
        return LocalDateTime.now().isAfter(expiredTs)
    }

    companion object{
        fun of(email: String, certificationNumber: String): CertMail {
            return CertMail(
                email = email,
                certificationNumber = certificationNumber,
                expiredTs = LocalDateTime.now().plusMinutes(3)
            )
        }
    }
}
