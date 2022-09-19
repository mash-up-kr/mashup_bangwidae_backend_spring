package kr.mashup.bangwidae.asked.controller.dto

import kr.mashup.bangwidae.asked.model.document.LevelPolicy

data class LevelPolicyDto (
    val level: Int,
    val wardCountCondition: Int,
    val answerCountCondition: Int,
    val questionCountCondition: Int,
    val maxWardCount: Int,
    val imageUrl: String
) {
    companion object {
        fun from(levelPolicy: LevelPolicy): LevelPolicyDto{
            return LevelPolicyDto(
                level = levelPolicy.level,
                wardCountCondition = levelPolicy.wardCountCondition,
                answerCountCondition = levelPolicy.answerCountCondition,
                questionCountCondition = levelPolicy.questionCountCondition,
                maxWardCount = levelPolicy.maxWardCount,
                imageUrl = levelPolicy.imageUrl
            )
        }
    }
}