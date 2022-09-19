package kr.mashup.bangwidae.asked.model.document

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
    val maxWardCount: Int,
    val imageUrl: String
) {
    fun isSatisfiedLevelUp(userWardCount: Int, userAnswerCount: Int, userQuestionCount: Int): Boolean {
        return userWardCount >= wardCountCondition &&
                userAnswerCount >= answerCountCondition &&
                userQuestionCount >= questionCountCondition
    }
}