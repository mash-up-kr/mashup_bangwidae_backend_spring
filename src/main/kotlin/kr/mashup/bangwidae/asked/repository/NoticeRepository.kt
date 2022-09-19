package kr.mashup.bangwidae.asked.repository

import kr.mashup.bangwidae.asked.model.Notice
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.MongoRepository

interface NoticeRepository: MongoRepository<Notice, ObjectId> {
    fun findAllByIdBeforeAndDeletedFalseOrderByIdDesc(lastId: ObjectId, pageRequest: PageRequest): List<Notice>
}