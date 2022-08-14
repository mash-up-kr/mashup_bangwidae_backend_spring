package kr.mashup.bangwidae.asked.model.document.question

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

@Document("answer")
data class Answer(
    @Id
    val id: ObjectId? = null,
    val userId: ObjectId,
    val questionId: ObjectId,
    val content: String,

    // TODO location 기존 데이터 마이그레이션 후 not-null 로 수정
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    val location: GeoJsonPoint?,
    val representativeAddress: String? = null,
    val region: Region? = null,

    val deleted: Boolean = false,
    @Version
    var version: Int? = null,
    @CreatedDate var createdAt: LocalDateTime? = null,
    @LastModifiedDate var updatedAt: LocalDateTime? = null,
) {
    fun updateContent(content: String): Answer {
        return this.copy(
            content = content,
        )
    }

    fun delete(): Answer {
        return this.copy(
            deleted = true,
        )
    }
}
