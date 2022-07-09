package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.Ward
import kr.mashup.bangwidae.asked.repository.WardRepository
import kr.mashup.bangwidae.asked.utils.GeoUtils
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class WardService(
    private val wardRepository: WardRepository
) {
    fun createWard(user: User, name: String, longitude: Double, latitude: Double): Boolean {
        // Todo Level 당 와드 검사
        val ward = Ward.create(
            userId = user.id!!,
            name = name,
            location = GeoUtils.geoJsonPoint(longitude, latitude)
        )
        wardRepository.save(ward)
        return true
    }

    fun getMyWards(user: User): List<Ward> {
        return wardRepository.findAllByUserIdAndExpiredAtAfter(user.id!!, LocalDateTime.now())
    }
}