package kr.mashup.bangwidae.asked.service.post

import kr.mashup.bangwidae.asked.controller.dto.CommentDto
import kr.mashup.bangwidae.asked.controller.dto.CursorResult
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.post.Comment
import kr.mashup.bangwidae.asked.model.post.Post
import kr.mashup.bangwidae.asked.repository.CommentRepository
import kr.mashup.bangwidae.asked.repository.UserRepository
import kr.mashup.bangwidae.asked.service.place.PlaceService
import kr.mashup.bangwidae.asked.utils.getLatitude
import kr.mashup.bangwidae.asked.utils.getLongitude
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import kotlin.math.min


@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val placeService: PlaceService
) : WithPostAuthorityValidator, WithCommentAuthorityValidator {
    fun findById(commentId: ObjectId): Comment {
        return commentRepository.findByIdAndDeletedFalse(commentId)
            ?: throw DoriDoriException.of(DoriDoriExceptionType.NOT_EXIST)
    }

    fun getCommentsByPostId(
        postId: ObjectId, lastId: ObjectId?, size: Int
    ): CursorResult<CommentDto> {
        val commentList = commentRepository.findByPostIdAndIdBeforeAndDeletedFalseOrderByIdDesc(
            postId,
            lastId ?: ObjectId(),
            PageRequest.of(0, size + 1)
        )
        val userIdList = commentList.map { it.userId }.distinct()
        val userMap = userRepository.findAllByIdIn(userIdList).associateBy { it.id }
        return CursorResult(
            values = commentList.subList(0, min(commentList.size, size))
                .map { CommentDto.from(userMap[it.userId]!!, it) },
            hasNext = (commentList.size == size + 1)
        )
    }

    fun write(user: User, post: Post, comment: Comment): Comment {
        post.validateToComment(user)
        return commentRepository.save(updatePlaceInfo(comment))
    }

    fun edit(user: User, comment: Comment): Comment {
        comment.validateToUpdate(user)
        return commentRepository.save(updatePlaceInfo(comment))
    }

    fun delete(user: User, comment: Comment): Comment {
        comment.validateToDelete(user)
        return commentRepository.save(comment.delete())
    }

    private fun updatePlaceInfo(comment: Comment): Comment {
        val longitude = comment.location.getLongitude()
        val latitude = comment.location.getLatitude()
        val region = placeService.reverseGeocode(longitude, latitude)
        return comment.copy(region = region)
    }
}