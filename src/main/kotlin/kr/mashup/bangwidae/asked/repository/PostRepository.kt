package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.post.Post
import org.bson.types.ObjectId
import org.springframework.data.geo.Distance
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : MongoRepository<Post, ObjectId> {
	fun findAByLocationNear(location: GeoJsonPoint, distance: Distance): List<Post>
}