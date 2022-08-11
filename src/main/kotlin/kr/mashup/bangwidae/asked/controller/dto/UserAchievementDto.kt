package kr.mashup.bangwidae.asked.controller.dto

import kr.mashup.bangwidae.asked.service.levelpolicy.UserAchievement

data class UserAchievementDto(
    val userWardCount: Int,
    val userAnswerCount: Int,
    val userQuestionCount: Int
) {
    companion object {
        fun from(userAchievement: UserAchievement): UserAchievementDto {
            return UserAchievementDto(
                userAnswerCount = userAchievement.userAnswerCount,
                userWardCount = userAchievement.userWardCount,
                userQuestionCount = userAchievement.userQuestionCount
            )
        }
    }
}