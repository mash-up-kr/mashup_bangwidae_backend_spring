package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.CertMail
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
        val certMail = (certMailRepository.findFirstByEmailAndOrderByMailSendTsDesc(email)
            ?: throw DoriDoriException.of(DoriDoriExceptionType.COMMON_ERROR))

        if (!certMail.isCertifiedBy(certificationNumber)) {
            throw DoriDoriException.of(DoriDoriExceptionType.CERTIFICATE_FAILED)
        }

        certMail.certificationTs = LocalDateTime.now()
        certMailRepository.save(certMail)
    }
}