package kr.mashup.bangwidae.asked.service.question

import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.repository.*
import org.springframework.stereotype.Service

@Service
class LevelPolicyService(
    private val userRepository: UserRepository,
    private val wardRepository: WardRepository,
    private val answerRepository: AnswerRepository,
    private val questionRepository: QuestionRepository,
    private val levelPolicyRepository: LevelPolicyRepository,
) {

    fun levelUpIfConditionSatisfied(user: User) {
        // 다음 레벨 policy 없으면 만렙임
        val policy = levelPolicyRepository.findByLevel(user.level + 1)?: return

        val wardCount = wardRepository.countAllByUserId(user.id!!)
        val answerCount = answerRepository.countAllByUserId(user.id)
        val questionCount = questionRepository.countAllByFromUserId(user.id)

        val satisfiedLevelUp = policy.isSatisfiedLevelUp(
            userWardCount = wardCount,
            userAnswerCount = answerCount,
            userQuestionCount = questionCount
        )

        if (satisfiedLevelUp) {
            userRepository.save(user.levelUp())
        }
    }
}