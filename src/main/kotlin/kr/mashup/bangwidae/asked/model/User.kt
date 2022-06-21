package kr.mashup.bangwidae.asked.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("user")
data class User(
	val id: ObjectId? = null,
	val nickname: String = "noname",
	val loginId: String,
	val password: String? = null,
	val providerId: String? = null,
	val loginType: LoginType,
	val tags: List<String> = emptyList(),

	@CreatedDate // TODO: KST 변환방법 알아보기 JAVA TIME Module
	val createdAt: LocalDateTime = LocalDateTime.MIN, // TODO: modified, created 되는지 테스트해보기
	@LastModifiedDate
	val updatedAt: LocalDateTime = LocalDateTime.MIN,
) {
	companion object {
		fun createBasicUser(loginId: String, password: String): User {
			return User(
				loginId = loginId,
				password = password,
				loginType = LoginType.BASIC
			)
		}
	}
}

enum class LoginType {
	BASIC, APPLE, KAKAO
}