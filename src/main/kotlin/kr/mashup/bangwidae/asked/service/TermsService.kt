package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.model.document.terms.Terms
import kr.mashup.bangwidae.asked.model.document.terms.TermsAgreement
import kr.mashup.bangwidae.asked.repository.TermsAgreementRepository
import kr.mashup.bangwidae.asked.repository.TermsRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TermsService(
    private val termsRepository: TermsRepository,
    private val termsAgreementRepository: TermsAgreementRepository,
) {

    fun getTerms(): List<Terms> {
        return termsRepository.findAllByDeletedFalse().sortedBy { it.priority }
    }

    fun agreeTerms(user: User, termsIds: List<ObjectId>) {
        val termsList = termsRepository.findAllByIdIn(termsIds)

        val agreements = termsList.map {
            TermsAgreement(
                termsId = it.id!!,
                userId = user.id!!,
                agreementTime = LocalDateTime.now(),
            )
        }

        termsAgreementRepository.saveAll(agreements)
    }
}