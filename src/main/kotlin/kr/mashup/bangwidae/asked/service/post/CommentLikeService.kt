package kr.mashup.bangwidae.asked.service.post

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.post.Comment
import kr.mashup.bangwidae.asked.model.post.CommentLike
import kr.mashup.bangwidae.asked.repository.CommentLikeRepository
import kr.mashup.bangwidae.asked.repository.CommentRepository
import kr.mashup.bangwidae.asked.service.LevelPolicyService
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class CommentLikeService(
    private val commentLikeRepository: CommentLikeRepository,
    private val commentRepository: CommentRepository,
    private val levelPolicyService: LevelPolicyService,
) {
    fun commentLike(commentId: ObjectId, user: User) {
        require(commentRepository.existsByIdAndDeletedFalse(commentId)) {
            throw DoriDoriException.of(DoriDoriExceptionType.NOT_EXIST)
        }
        if (!commentLikeRepository.existsByCommentIdAndUserId(commentId, user.id!!)) {
            commentLikeRepository.save(CommentLike(userId = user.id, commentId = commentId)).also {
                levelPolicyService.levelUpIfConditionSatisfied(user)
            }
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