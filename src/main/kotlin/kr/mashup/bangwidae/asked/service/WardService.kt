package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.Ward
import kr.mashup.bangwidae.asked.repository.LevelPolicyRepository
import kr.mashup.bangwidae.asked.repository.WardRepository
import kr.mashup.bangwidae.asked.service.levelpolicy.LevelPolicyService
import kr.mashup.bangwidae.asked.utils.GeoUtils
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class WardService(
    private val wardRepository: WardRepository,
    private val levelPolicyRepository: LevelPolicyRepository,
    private val levelPolicyService: LevelPolicyService
) {
    fun createWard(user: User, name: String, longitude: Double, latitude: Double): Boolean {
        val userLevelPolicy = levelPolicyRepository.findByLevel(user.level)
        val userWardCount = wardRepository.countAllByUserId(user.id!!)

        if (userWardCount >= userLevelPolicy!!.maxWardCount) {
            throw DoriDoriException.of(DoriDoriExceptionType.WARD_MAX_COUNT)
        }

        val ward = Ward.create(
            userId = user.id,
            name = name,
            location = GeoUtils.geoJsonPoint(longitude, latitude)
        )
        wardRepository.save(ward)
        levelPolicyService.levelUpIfConditionSatisfied(user)
        return true
    }

    fun getMyWards(user: User): List<Ward> {
        return wardRepository.findAllByUserIdAndExpiredAtAfter(user.id!!, LocalDateTime.now())
    }

    fun getMyRepresentativeWard(user: User): Ward? {
        return getMyWards(user).firstOrNull { it.isRepresentative == true }
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

    fun updateRepresentativeWard(user: User, wardId: ObjectId?) {
        wardRepository.saveAll(getMyWards(user).map {it.copy(isRepresentative = (it.id == wardId))})
    }
}