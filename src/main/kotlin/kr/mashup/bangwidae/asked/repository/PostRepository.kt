package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.document.post.Post
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.data.geo.Distance
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : MongoRepository<Post, ObjectId> {
    fun countAllByUserIdAndDeletedFalse(userId: ObjectId): Int
    fun findByLocationNearAndIdBeforeAndDeletedFalseOrderByIdDesc(
        location: GeoJsonPoint,
        lastId: ObjectId,
        distance: Distance,
        pageRequest: PageRequest
    ): List<Post>

    fun existsByIdAndDeletedFalse(id: ObjectId): Boolean
    fun findByIdAndDeletedFalse(id: ObjectId): Post?
    fun findByLocationNear(location: GeoJsonPoint, distance: Distance): List<Post>
    fun findByUserIdAndIdBeforeAndDeletedFalseOrderByIdDesc(
        userId: ObjectId,
        lastId: ObjectId,
        pageRequest: PageRequest,
    ): List<Post>
}