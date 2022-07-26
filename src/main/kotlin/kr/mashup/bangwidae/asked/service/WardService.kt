package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.Ward
import kr.mashup.bangwidae.asked.repository.WardRepository
import kr.mashup.bangwidae.asked.utils.GeoUtils
import org.bson.types.ObjectId
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

    fun deleteWard(user: User, wardId: ObjectId) {
        val ward = wardRepository.findById(wardId)
            .orElseThrow { DoriDoriException.of(DoriDoriExceptionType.WARD_NOT_FOUND) }

        if (ward.userId != user.id!!) {
            throw DoriDoriException.of(DoriDoriExceptionType.NOT_ALLOWED_TO_ACCESS)
        }
        
        wardRepository.delete(ward)
    }

    fun extendWardPeriod(user: User, wardId: ObjectId, period: Int): Ward {
        val ward = wardRepository.findById(wardId)
            .orElseThrow { DoriDoriException.of(DoriDoriExceptionType.WARD_NOT_FOUND) }

        if (ward.userId != user.id!!) {
            throw DoriDoriException.of(DoriDoriExceptionType.NOT_ALLOWED_TO_ACCESS)
        }
        return ward.extendPeriod(period).let {
            wardRepository.save(it)
        }
    }
}