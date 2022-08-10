package kr.mashup.bangwidae.asked.service.post

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.post.Post
import kr.mashup.bangwidae.asked.model.post.PostLike
import kr.mashup.bangwidae.asked.repository.PostLikeRepository
import kr.mashup.bangwidae.asked.repository.PostRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class PostLikeService(
    private val postLikeRepository: PostLikeRepository,
    private val postRepository: PostRepository,
) {
    fun postLike(postId: ObjectId, user: User) {
        require(postRepository.existsByIdAndDeletedFalse(postId)) {
            throw DoriDoriException.of(DoriDoriExceptionType.NOT_EXIST)
        }
        if (!postLikeRepository.existsByPostIdAndUserId(postId, user.id!!)) {
            postLikeRepository.save(PostLike(userId = user.id, postId = postId))
        }
    }

    fun postUnlike(postId: ObjectId, userId: ObjectId) {
        postLikeRepository.findByPostIdAndUserId(postId, userId)?.let {
            postLikeRepository.delete(it)
        }
    }

    fun getLikeMap(postList: List<Post>): Map<ObjectId, List<PostLike>> {
        val postLikeList = postLikeRepository.findAllByPostIdIn(postList.mapNotNull { it.id })
        return postLikeList.groupBy { it.postId }
    }
}