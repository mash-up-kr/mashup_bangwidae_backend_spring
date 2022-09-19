package kr.mashup.bangwidae.asked.controller.dto

import kr.mashup.bangwidae.asked.model.domain.WriterUserDomain

data class WriterUserDto(
    val id: String,
    val tags: List<String> = emptyList(),
    val nickname: String,
    val profileImageUrl: String?,
    val level: Int
) {
    companion object {
        fun from(user: WriterUserDomain): WriterUserDto {
            return WriterUserDto(
                id = user.id.toHexString(),
                tags = user.tags,
                nickname = user.nickname,
                profileImageUrl = user.profileImageUrl,
                level = user.level
            )
        }
    }
}