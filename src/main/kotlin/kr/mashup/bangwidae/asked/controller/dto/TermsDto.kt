package kr.mashup.bangwidae.asked.controller.dto

import kr.mashup.bangwidae.asked.model.document.terms.Terms
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class TermsDto(
    val id: ObjectId,
    val title: String,
    val content: String,
    val necessary: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(terms: Terms): TermsDto {
            return TermsDto(
                id = terms.id!!,
                title = terms.title,
                content = terms.content,
                necessary = terms.isNecessary,
                createdAt = terms.createdAt!!,
                updatedAt = terms.updatedAt!!,
            )
        }
    }
}