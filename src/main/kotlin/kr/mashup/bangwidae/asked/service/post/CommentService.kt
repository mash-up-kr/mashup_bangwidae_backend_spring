package kr.mashup.bangwidae.asked.service.post

import kr.mashup.bangwidae.asked.controller.dto.CommentEditRequest
import kr.mashup.bangwidae.asked.controller.dto.CommentWriteRequest
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.post.Comment
import kr.mashup.bangwidae.asked.repository.CommentRepository
import kr.mashup.bangwidae.asked.repository.PostRepository
import org.bson.types.ObjectId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service


@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository
) : WithPostAuthorityValidator, WithCommentAuthorityValidator {
    fun findById(commentId: ObjectId): Comment {
        return commentRepository.findByIdAndDeletedFalse(commentId)
            ?: throw DoriDoriException.of(DoriDoriExceptionType.NOT_EXIST)
    }

    fun write(user: User, postId: ObjectId, request: CommentWriteRequest): Comment {
        postRepository.findByIdOrNull(postId)
            ?.also { it.validateToComment(user) }
            ?: throw DoriDoriException.of(DoriDoriExceptionType.NOT_EXIST)

        return commentRepository.save(
            Comment(
                userId = user.id!!,
                postId = postId,
                content = request.content,
            )
        )
    }

    fun edit(user: User, commentId: ObjectId, request: CommentEditRequest): Comment {
        val comment = findById(commentId)
            .also { it.validateToUpdate(user) }

        return commentRepository.save(
            comment.updateContent(request.content)
        )
    }

    fun delete(user: User, commentId: ObjectId): Comment {
        val answer = findById(commentId)
            .also { it.validateToDelete(user) }

        return commentRepository.save(
            answer.delete()
        )
    }
}