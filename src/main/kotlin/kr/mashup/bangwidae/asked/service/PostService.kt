package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.controller.dto.CursorResult
import kr.mashup.bangwidae.asked.controller.dto.PostDto
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.post.Post
import kr.mashup.bangwidae.asked.repository.PostRepository
import kr.mashup.bangwidae.asked.service.place.PlaceService
import kr.mashup.bangwidae.asked.utils.GeoUtils
import kr.mashup.bangwidae.asked.utils.getLatitude
import kr.mashup.bangwidae.asked.utils.getLongitude
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository: PostRepository, private val placeService: PlaceService
) {
    fun findById(id: ObjectId): Post {
        return postRepository.findById(id).orElseThrow { DoriDoriException.of(DoriDoriExceptionType.NOT_EXIST) }
    }

    fun save(post: Post): Post {
        return postRepository.save(updatePlaceInfo(post))
    }

    fun update(user: User, post: Post): Post {
        require(isPostValidForUser(post, user)) {
            throw DoriDoriException.of(DoriDoriExceptionType.POST_NOT_ALLOWED_FOR_USER)
        }
        return postRepository.save(updatePlaceInfo(post))
    }

    fun delete(user: User, post: Post) {
        require(isPostValidForUser(post, user)) {
            throw DoriDoriException.of(DoriDoriExceptionType.POST_NOT_ALLOWED_FOR_USER)
        }
        postRepository.save(post.copy(deleted = true))
    }

    fun getNearPost(
        longitude: Double, latitude: Double, meterDistance: Double, lastId: ObjectId?, size: Int
    ): CursorResult<PostDto> {
        val postList = postRepository.findByLocationNearAndIdBeforeAndDeletedFalseOrderByIdDesc(
            GeoUtils.geoJsonPoint(longitude, latitude),
            lastId ?: ObjectId(),
            Distance(meterDistance / 1000, Metrics.KILOMETERS),
            PageRequest.of(0, size)
        )
        return CursorResult(postList.map { PostDto.from(it) }, hasNext(postList.last().id))
    }

    private fun hasNext(id: ObjectId?): Boolean {
        if (id == null) return false
        return postRepository.existsByIdBeforeAndDeletedFalse(id, false)
    }

    private fun updatePlaceInfo(post: Post): Post {
        val longitude = post.location.getLongitude()
        val latitude = post.location.getLatitude()
        val region = placeService.reverseGeocode(longitude, latitude)
        val representativeAddress = placeService.getRepresentativeAddress(longitude, latitude)
        return post.copy(representativeAddress = representativeAddress, region = region)
    }

    private fun isPostValidForUser(post: Post, user: User): Boolean {
        return post.userId == user.id!!
    }
}