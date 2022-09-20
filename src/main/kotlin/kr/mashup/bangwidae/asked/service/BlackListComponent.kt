package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.repository.BlackListRepository
import org.bson.types.ObjectId
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference
import javax.annotation.PostConstruct

@Component
class BlackListComponent(
    private val blackListRepository: BlackListRepository
) {
    private val blackListMap: AtomicReference<ConcurrentHashMap<ObjectId, List<ObjectId>>> = AtomicReference()

    @PostConstruct
    fun init() {
        setBlackListMap()
    }

    fun setBlackListMap() {
        ConcurrentHashMap<ObjectId, List<ObjectId>>().apply {
            putAll(blackListRepository.findAll().groupBy({ it.fromUserId }, { it.toUserId }))
        }.let { blackListMap.set(it) }
    }

    fun getBlackListMap() = blackListMap
}