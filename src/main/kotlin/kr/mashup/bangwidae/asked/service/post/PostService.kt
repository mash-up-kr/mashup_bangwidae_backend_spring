package kr.mashup.bangwidae.asked.service.post

import kr.mashup.bangwidae.asked.controller.dto.CursorResult
import kr.mashup.bangwidae.asked.controller.dto.PostDto
import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.post.Post
import kr.mashup.bangwidae.asked.repository.PostRepository
import kr.mashup.bangwidae.asked.repository.UserRepository
import kr.mashup.bangwidae.asked.service.place.PlaceService
import kr.mashup.bangwidae.asked.utils.GeoUtils
import kr.mashup.bangwidae.asked.utils.getLatitude
import kr.mashup.bangwidae.asked.utils.getLongitude
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository: PostRepository,
    private val placeService: PlaceService,
    private val userRepository: UserRepository
) : WithPostAuthorityValidator {
    fun findById(id: ObjectId): Post {
        return postRepository.findByIdAndDeletedFalse(id)
            ?: throw DoriDoriException.of(DoriDoriExceptionType.NOT_EXIST)
    }

    fun write(post: Post): Post {
        return postRepository.save(updatePlaceInfo(post))
    }

    fun update(user: User, post: Post): Post {
        post.validateToUpdate(user)
        return postRepository.save(updatePlaceInfo(post))
    }

    fun delete(user: User, post: Post) {
        post.validateToDelete(user)
        postRepository.save(post.delete())
    }

    fun getNearPost(
        longitude: Double, latitude: Double, meterDistance: Double, lastId: ObjectId?, size: Int
    ): CursorResult<PostDto> {
        val location = GeoUtils.geoJsonPoint(longitude, latitude)
        val distance = Distance(meterDistance / 1000, Metrics.KILOMETERS)
        val postList = postRepository.findByLocationNearAndIdBeforeAndDeletedFalseOrderByIdDesc(
            location,
            lastId ?: ObjectId(),
            distance,
            PageRequest.of(0, size)
        )
        val userMap = userRepository.findAllByIdIn(postList.map { it.userId }).associateBy { it.id }
        return CursorResult(
            postList.map { PostDto.from(userMap[it.userId]!!, it) },
            hasNext(location, postList.last().id, distance)
        )
    }

    fun getPostById(id: ObjectId): PostDto {
        val post = findById(id)
        val user = userRepository.findByIdOrNull(post.userId)
            ?: throw DoriDoriException.of(DoriDoriExceptionType.POST_WRITER_USER_NOT_EXIST)
        return PostDto.from(user, post)
    }

    private fun hasNext(location: GeoJsonPoint, id: ObjectId?, distance: Distance): Boolean {
        if (id == null) return false
        return postRepository.findByLocationNearAndIdBeforeAndDeletedFalseOrderByIdDesc(
            location,
            id,
            distance,
            PageRequest.of(0, 1)
        ).isNotEmpty()
    }

    private fun updatePlaceInfo(post: Post): Post {
        val longitude = post.location.getLongitude()
        val latitude = post.location.getLatitude()
        val region = placeService.reverseGeocode(longitude, latitude)
        val representativeAddress = placeService.getRepresentativeAddress(longitude, latitude)
        return post.copy(representativeAddress = representativeAddress, region = region)
    }
}