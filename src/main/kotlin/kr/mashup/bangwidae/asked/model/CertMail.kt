package kr.mashup.bangwidae.asked.model

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("cert-mail")
data class CertMail(
    val id: ObjectId? = null,
    val email: String,
    val certificationNumber: String,
    val certificationTs: LocalDateTime? = null,
    val expiredTs: LocalDateTime? = null
) {
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
