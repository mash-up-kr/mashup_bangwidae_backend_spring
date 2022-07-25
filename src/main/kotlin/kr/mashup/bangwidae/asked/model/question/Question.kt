package kr.mashup.bangwidae.asked.model.question

import kr.mashup.bangwidae.asked.model.Region
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("question")
data class Question(
    @Id
    val id: ObjectId? = null,
    val toUserId: ObjectId,
    val fromUserId: ObjectId,
    val content: String,
    val status: QuestionStatus = QuestionStatus.ANSWER_WAITING,

    // TODO location 기존 데이터 마이그레이션 후 not-null 로 수정
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    val location: GeoJsonPoint?,
    val representativeAddress: String? = null,
    val region: Region? = null,

    val deleted: Boolean = false,
    @Version var version: Int? = null,
    @CreatedDate var createdAt: LocalDateTime? = null,
    @LastModifiedDate var updatedAt: LocalDateTime? = null,
) {
    fun updateContent(content: String): Question {
        return this.copy(
            content = content,
        )
    }

    fun deny(): Question {
        return this.copy(
            status = QuestionStatus.QUESTION_DENY,
        )
    }

    fun delete(): Question {
        return this.copy(
            deleted = true,
        )
    }

    fun answer(): Question {
        return this.copy(
            status = QuestionStatus.ANSWER_COMPLETE,
        )
    }

    fun waiting(): Question {
        return this.copy(
            status = QuestionStatus.ANSWER_WAITING,
        )
    }
}

enum class QuestionStatus {
    // 답변 대기
    ANSWER_WAITING,

    // 답변 완료
    ANSWER_COMPLETE,

    // 질문 거절
    QUESTION_DENY,
    ;
}
