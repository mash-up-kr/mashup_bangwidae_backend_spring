package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.BlackList
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface BlackListRepository: MongoRepository<BlackList, ObjectId> {
}