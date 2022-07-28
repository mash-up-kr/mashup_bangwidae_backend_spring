package kr.mashup.bangwidae.asked.service.post

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.post.Comment
import kr.mashup.bangwidae.asked.model.post.CommentLike
import kr.mashup.bangwidae.asked.repository.CommentLikeRepository
import kr.mashup.bangwidae.asked.repository.CommentRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class CommentLikeService(
    private val commentLikeRepository: CommentLikeRepository,
    private val commentRepository: CommentRepository
) {
    fun commentLike(commentId: ObjectId, userId: ObjectId) {
        require(commentRepository.existsByIdAndDeletedFalse(commentId)) {
            throw DoriDoriException.of(DoriDoriExceptionType.NOT_EXIST)
        }
        if (!commentLikeRepository.existsByCommentIdAndUserId(commentId, userId)) {
            commentLikeRepository.save(CommentLike(userId = userId, commentId = commentId))
        }
    }

    fun commentUnlike(commentId: ObjectId, userId: ObjectId) {
        commentLikeRepository.findByCommentIdAndUserId(commentId, userId)?.let {
            commentLikeRepository.delete(it)
        }
    }

    fun getLikeMap(commentList: List<Comment>): Map<ObjectId, List<CommentLike>> {
        val commentLikeList = commentLikeRepository.findAllByCommentIdIn(commentList.mapNotNull { it.id })
        return commentLikeList.groupBy { it.commentId }
    }
}