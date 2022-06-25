package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.model.CertMail
import kr.mashup.bangwidae.asked.repository.CertMailRepository
import org.springframework.stereotype.Service

@Service
class CertMailService(
    private val certMailRepository: CertMailRepository
) {
    fun create(email: String, certificationNumber: String): CertMail {
        val mailSendLog = CertMail.of(email = email, certificationNumber = certificationNumber)
        return certMailRepository.save(mailSendLog)
    }
}