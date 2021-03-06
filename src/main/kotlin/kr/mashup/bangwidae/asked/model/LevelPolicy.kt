package kr.mashup.bangwidae.asked.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("level-policy")
data class LevelPolicy (
    @Id
    val id: ObjectId? = null,
    val level: Int,
    val wardCountCondition: Int,
    val answerCountCondition: Int,
    val questionCountCondition: Int,
    val wardCount: Int,
) {
    fun isSatisfiedLevelUp(userWardCount: Int, userAnswerCount: Int, userQuestionCount: Int): Boolean {
        return userWardCount >= wardCount &&
                userAnswerCount >= answerCountCondition &&
                userQuestionCount >= questionCountCondition
    }
}