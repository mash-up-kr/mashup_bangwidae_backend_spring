package kr.mashup.bangwidae.asked.model.post

import kr.mashup.bangwidae.asked.controller.dto.PostEditRequest
import kr.mashup.bangwidae.asked.service.place.Region
import kr.mashup.bangwidae.asked.utils.GeoUtils
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("post")
data class Post(
    val id: ObjectId? = null,
    val userId: ObjectId,
    val content: String = "",
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE) val location: GeoJsonPoint,
    val representativeAddress: String? = null,
    val region: Region? = null,

    @CreatedDate val createdAt: LocalDateTime = LocalDateTime.now(),
    @LastModifiedDate val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun update(postEditRequest: PostEditRequest): Post {
        return postEditRequest.let {
            this.copy(
                content = it.content ?: this.content,
                location = if (it.longitude != null && it.latitude != null)
                    GeoUtils.geoJsonPoint(it.longitude, it.latitude) else this.location
            )
        }
    }
}