package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.model.document.Ward
import kr.mashup.bangwidae.asked.repository.LevelPolicyRepository
import kr.mashup.bangwidae.asked.repository.WardRepository
import kr.mashup.bangwidae.asked.service.levelpolicy.LevelPolicyService
import kr.mashup.bangwidae.asked.service.place.PlaceService
import kr.mashup.bangwidae.asked.utils.GeoUtils
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class WardService(
    private val wardRepository: WardRepository,
    private val levelPolicyRepository: LevelPolicyRepository,
    private val levelPolicyService: LevelPolicyService,
    private val placeService: PlaceService
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
            location = GeoUtils.geoJsonPoint(longitude, latitude),
            region = placeService.reverseGeocode(longitude, latitude)
        )
        wardRepository.save(ward)
        levelPolicyService.levelUpIfConditionSatisfied(user)
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

    fun getMyRepresentativeWard(user: User): Ward? {
        return wardRepository.findWardByUserIdAndExpiredAtAfterAndIsRepresentativeTrue(user.id!!, LocalDateTime.now())
    }

    fun updateRepresentativeWard(user: User, representativeWardId: ObjectId?) {
        getMyRepresentativeWard(user)?.let { wardRepository.save(it.copy(isRepresentative = false)) }
        if (representativeWardId != null) {
            wardRepository.findWardByIdAndExpiredAtAfter(representativeWardId, LocalDateTime.now())?.let {
                wardRepository.save(it.copy(isRepresentative = true))
            }
        }
    }
}