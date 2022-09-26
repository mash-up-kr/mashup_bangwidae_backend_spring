package kr.mashup.bangwidae.asked.model.document.post

import kr.mashup.bangwidae.asked.controller.dto.PostEditRequest
import kr.mashup.bangwidae.asked.model.Region
import kr.mashup.bangwidae.asked.utils.GeoUtils
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

@Document("post")
data class Post(
    @Id
    val id: ObjectId? = null,
    val userId: ObjectId,
    val content: String = "",
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    val location: GeoJsonPoint,
    val representativeAddress: String? = null,
    val region: Region? = null,
    val anonymous: Boolean,

    val deleted: Boolean = false,
    @Version var version: Int? = null,
    @CreatedDate var createdAt: LocalDateTime? = null,
    @LastModifiedDate var updatedAt: LocalDateTime? = null
) {
    fun update(postEditRequest: PostEditRequest): Post {
        return postEditRequest.let {
            this.copy(
                content = it.content ?: this.content,
                location = if (it.longitude != null && it.latitude != null)
                    GeoUtils.geoJsonPoint(it.longitude, it.latitude) else this.location,
                anonymous = it.anonymous ?: this.anonymous
            )
        }
    }

    fun delete(): Post {
        return this.copy(
            deleted = true,
        )
    }

    fun toBlockedPost() = this.copy(
        content = "차단된 사용자의 글입니다"
    )

    fun toReportedPost() = this.copy(
        content = "신고된 글입니다"
    )
}