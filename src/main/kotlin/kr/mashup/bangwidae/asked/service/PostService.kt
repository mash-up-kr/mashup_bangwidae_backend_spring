package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.controller.dto.CursorResult
import kr.mashup.bangwidae.asked.controller.dto.PostDto
import kr.mashup.bangwidae.asked.model.post.Post
import kr.mashup.bangwidae.asked.repository.PostRepository
import kr.mashup.bangwidae.asked.utils.GeoUtils
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.stereotype.Service

@Service
class PostService(
	private val postRepository: PostRepository
) {
	fun upsert(post: Post): Post {
		return postRepository.save(
			post.copy(
				representativeAddress = "", //TODO: 역 geocode 결과 반영
				fullAddress = ""  //TODO: 역 geocode 결과 반영
			)
		)
	}

	fun getNearPost(
		longitude: Double,
		latitude: Double,
		meterDistance: Double,
		lastId: ObjectId?,
		size: Int
	): CursorResult<PostDto> {
		val postList = postRepository.findByLocationNearAndIdBeforeOrderByIdDesc(
			GeoUtils.geoJsonPoint(longitude, latitude),
			lastId ?: ObjectId(),
			Distance(meterDistance / 1000, Metrics.KILOMETERS),
			PageRequest.of(0, size)
		)
		return CursorResult(postList.map { PostDto.from(it) }, hasNext(postList.last().id))
	}

	private fun hasNext(id: ObjectId?): Boolean {
		if (id == null) return false
		return postRepository.existsByIdBefore(id)
	}
}