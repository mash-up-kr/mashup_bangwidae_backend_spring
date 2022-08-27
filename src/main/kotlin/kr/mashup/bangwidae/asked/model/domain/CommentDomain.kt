package kr.mashup.bangwidae.asked.model.domain

import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.model.document.post.Comment
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class CommentDomain(
    val id: ObjectId,
    val user: WriterUserDomain,
    val content: String,
    val likeCount: Int,
    val userLiked: Boolean,
    val representativeAddress: String,
    val anonymous: Boolean,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun from(user: User, comment: Comment, likeCount: Int, userLiked: Boolean) = CommentDomain(
            id = comment.id!!,
            content = comment.content,
            representativeAddress = comment.region?.representativeAddress ?: "",
            anonymous = comment.anonymous,
            createdAt = comment.createdAt,
            updatedAt = comment.updatedAt,
            user = WriterUserDomain.of(user, comment.anonymous),
            likeCount = likeCount,
            userLiked = userLiked,
        )
    }
}
