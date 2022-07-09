package kr.mashup.bangwidae.asked.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Duration
import java.time.LocalDateTime

@Document("ward")
data class Ward(
    val id: ObjectId? = null,
    val userId: ObjectId,
    val name: String,
    val expiredAt: LocalDateTime,
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    val location: GeoJsonPoint,
    @Version var version: Int? = null,
    @CreatedDate var createdAt: LocalDateTime? = null,
    @LastModifiedDate var updatedAt: LocalDateTime? = null
) {
    fun getDDays(): String {
        val remainingDay = (Duration.between(LocalDateTime.now(), expiredAt).toDays() + 1)
        return if (remainingDay == 0L) {
            "D-Day"
        } else {
            "D-$remainingDay"
        }
    }

    companion object {
        fun create(userId: ObjectId, name: String, location: GeoJsonPoint): Ward {
            return Ward(
                userId = userId,
                name = name,
                expiredAt = LocalDateTime.now().plusDays(20),
                location = location,
            )
        }
    }
}
