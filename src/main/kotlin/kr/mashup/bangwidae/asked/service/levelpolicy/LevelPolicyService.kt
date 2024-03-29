package kr.mashup.bangwidae.asked.service.levelpolicy

import kr.mashup.bangwidae.asked.exception.DoriDoriException
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.model.document.LevelPolicy
import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.repository.*
import kr.mashup.bangwidae.asked.service.event.LevelUpEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class LevelPolicyService(
    private val userRepository: UserRepository,
    private val wardRepository: WardRepository,
    private val answerRepository: AnswerRepository,
    private val questionRepository: QuestionRepository,
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    private val levelPolicyRepository: LevelPolicyRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {

    fun levelUpIfConditionSatisfied(user: User) {
        // 다음 레벨 policy 없으면 만렙임
        val policy = levelPolicyRepository.findByLevel(user.level + 1) ?: return

        val userAchievement = getUserAchievement(user)
        val satisfiedLevelUp = policy.isSatisfiedLevelUp(
            userWardCount = userAchievement.userWardCount,
            userAnswerCount = userAchievement.userAnswerCount,
            userQuestionCount = userAchievement.userQuestionCount
        )

        if (satisfiedLevelUp) {
            userRepository.save(user.levelUp())
            applicationEventPublisher.publishEvent(LevelUpEvent(user.id!!))
        }
    }

    fun getLevelPolicy(level: Int): LevelPolicy {
        return levelPolicyRepository.findByLevel(level) ?: throw DoriDoriException.of(DoriDoriExceptionType.NOT_EXIST)
    }

    fun getUserAchievement(user: User): UserAchievement {
        val wardCount = wardRepository.countAllByUserId(user.id!!)
        val answerCount = answerRepository.countAllByUserIdAndDeletedFalse(user.id)
        val questionCount = questionRepository.countAllByFromUserIdAndDeletedFalse(user.id)
        val commentAnswerCount = commentRepository.countAllByUserIdAndDeletedFalse(user.id)
        val postQuestionCount = postRepository.countAllByUserIdAndDeletedFalse(user.id)
        return UserAchievement(
            userLevel = user.level,
            userWardCount = wardCount,
            userAnswerCount = answerCount + commentAnswerCount,
            userQuestionCount = questionCount + postQuestionCount
        )
    }
}