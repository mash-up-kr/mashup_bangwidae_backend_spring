package kr.mashup.bangwidae.asked.controller.dto

import kr.mashup.bangwidae.asked.model.document.terms.Terms
import org.bson.types.ObjectId

data class TermsDto(
    val id: ObjectId,
    val title: String,
    val content: String,
) {
    companion object {
        fun from(terms: Terms): TermsDto {
            return TermsDto(
                id = terms.id!!,
                title = terms.title,
                content = terms.content,
            )
        }
    }
}