package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.LevelPolicy
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface LevelPolicyRepository: MongoRepository<LevelPolicy, ObjectId> {
    fun findByLevel(level: Int): LevelPolicy?
}