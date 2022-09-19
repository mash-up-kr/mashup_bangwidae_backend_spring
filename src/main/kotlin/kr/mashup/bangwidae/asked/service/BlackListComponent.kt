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

    @Scheduled(cron = "0 0/1 * * * *")
    fun cron() {
        setBlackListMap()
    }

    fun setBlackListMap() {
        blackListMap.set(
            blackListRepository.findAll()
                .groupBy({ it.fromUserId }, { it.toUserId }) as ConcurrentHashMap<ObjectId, List<ObjectId>>?
        )
    }

    fun getBlackListMap() = blackListMap
}