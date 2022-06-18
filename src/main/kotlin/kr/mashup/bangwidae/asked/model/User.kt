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
	val password: String,
	val providerId: String,
	val loginType: LoginType, // TODO: DB에 어떻게 저장되는지 체크해보기
	val tags: List<String> = emptyList(),

	@CreatedDate // TODO: KST 변환방법 알아보기 JAVA TIME Module
	val createdAt: LocalDateTime = LocalDateTime.MIN, // TODO: modified, created 되는지 테스트해보기
	@LastModifiedDate
	val updatedAt: LocalDateTime = LocalDateTime.MIN,
)

enum class LoginType {
	BASIC, APPLE, KAKAO
}