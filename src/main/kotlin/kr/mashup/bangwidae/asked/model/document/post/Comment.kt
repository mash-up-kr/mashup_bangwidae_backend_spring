package kr.mashup.bangwidae.asked.model.document.post

import kr.mashup.bangwidae.asked.controller.dto.CommentEditRequest
import kr.mashup.bangwidae.asked.model.Region
import kr.mashup.bangwidae.asked.utils.GeoUtils
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("comment")
data class Comment(
    @Id
    val id: ObjectId? = null,
    val userId: ObjectId,
    val postId: ObjectId,
    val content: String,
    val location: GeoJsonPoint,
    val region: Region? = null,
    val anonymous: Boolean,

    val deleted: Boolean = false,
    @Version
    var version: Int? = null,
    @CreatedDate var createdAt: LocalDateTime? = null,
    @LastModifiedDate var updatedAt: LocalDateTime? = null
) {
    fun update(commentEditRequest: CommentEditRequest): Comment {
        return commentEditRequest.let {
            this.copy(
                content = it.content ?: this.content,
                location = if (it.longitude != null && it.latitude != null)
                    GeoUtils.geoJsonPoint(it.longitude, it.latitude) else this.location,
                anonymous = it.anonymous ?: this.anonymous
            )
        }
    }

    fun delete(): Comment {
        return this.copy(
            deleted = true,
        )
    }

    fun toBlockedComment() = this.copy(
        content = "차단된 사용자의 댓글입니다"
    )
}