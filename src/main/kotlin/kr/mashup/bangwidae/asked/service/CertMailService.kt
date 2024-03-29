package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.document.CertMail
import kr.mashup.bangwidae.asked.repository.CertMailRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CertMailService(
    private val certMailRepository: CertMailRepository
) {
    fun create(email: String, certificationNumber: String): CertMail {
        val mailSendLog = CertMail.of(email = email, certificationNumber = certificationNumber)
        return certMailRepository.save(mailSendLog)
    }

    fun certificate(email: String, certificationNumber: String) {
        val certMail = (certMailRepository.findFirstByEmailOrderByMailSendTsDesc(email)
            ?: throw DoriDoriException.of(DoriDoriExceptionType.COMMON_ERROR))

        if (!certMail.isCertifiedBy(certificationNumber)) {
            throw DoriDoriException.of(DoriDoriExceptionType.CERTIFICATE_FAILED)
        }

        if (certMail.isExpired()) {
            throw DoriDoriException.of(DoriDoriExceptionType.CERT_MAIL_EXPIRED)
        }

        certMail.certificationTs = LocalDateTime.now()
        certMail.isCertificated = true
        certMailRepository.save(certMail)
    }

    fun findByEmail(email: String): CertMail {
        return certMailRepository.findFirstByEmailOrderByMailSendTsDesc(email)
            ?: throw DoriDoriException.of(DoriDoriExceptionType.NOT_EXIST)
    }
}