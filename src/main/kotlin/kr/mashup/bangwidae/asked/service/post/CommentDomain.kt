package kr.mashup.bangwidae.asked.service.post

import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.post.Comment
import java.time.LocalDateTime

data class CommentDomain(
    val id: String,
    val user: CommentUserDomain,
    val content: String,
    val likeCount: Int,
    val userLiked: Boolean,
    val representativeAddress: String?,
    val anonymous: Boolean,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun from(user: User, comment: Comment, likeCount: Int, userLiked: Boolean) = CommentDomain(
            id = comment.id!!.toHexString(),
            content = comment.content,
            representativeAddress = comment.region?.representativeAddress,
            anonymous = comment.anonymous ?: false,
            createdAt = comment.createdAt,
            updatedAt = comment.updatedAt,
            user = if (comment.anonymous == true) CommentUserDomain.from(user.getAnonymousUser())
            else CommentUserDomain.from(user),
            likeCount = likeCount,
            userLiked = userLiked,
        )
    }
}

data class CommentUserDomain(
    val id: String,
    val tags: List<String>,
    val nickname: String,
    val profileImageUrl: String,
    val level: Int
) {
    companion object {
        fun from(user: User) = CommentUserDomain(
            id = user.id!!.toHexString(),
            nickname = user.nickname!!,
            tags = user.tags,
            profileImageUrl = user.userProfileImageUrl,
            level = user.level
        )
    }
}