package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.document.question.AnswerLike
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Component

@Component
class AnswerLikeAggregator(
    private val mongoTemplate: MongoTemplate,
) {
    fun getCountGroupByAnswerId(answerIds: Collection<ObjectId>): Map<ObjectId, Long> {
        val aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where(AnswerLike::answerId.name).`in`(answerIds)),
            Aggregation.group(AnswerLike::answerId.name).count().`as`(AnswerLikeCountGroupByAnswerId::likeCount.name),
        )

        val groupResults = mongoTemplate
            .aggregate(aggregation, AnswerLike::class.java, AnswerLikeCountGroupByAnswerId::class.java)

        return groupResults.mappedResults
            .associateBy { it.answerId }
            .mapValues { it.value.likeCount }
    }

    fun getCountByAnswerId(answerId: ObjectId): Long {
        return getCountGroupByAnswerId(listOf(answerId))[answerId] ?: 0
    }
}

data class AnswerLikeCountGroupByAnswerId(
    @Id val answerId: ObjectId,
    val likeCount: Long,
)