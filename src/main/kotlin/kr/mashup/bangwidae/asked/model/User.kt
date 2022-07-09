package kr.mashup.bangwidae.asked.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("user")
data class User(
    val id: ObjectId? = null,
    val nickname: String? = null,
    val email: String,
    val password: String? = null,
    val providerId: String? = null,
    val loginType: LoginType,
    val description: String? = null,
    val tags: List<String> = emptyList(),
    val profileImageUrl: String? = null,
    val refreshToken: String? = null,

    @CreatedDate // TODO: KST 변환방법 알아보기 JAVA TIME Module
    val createdAt: LocalDateTime = LocalDateTime.now(), // TODO: modified, created 되는지 테스트해보기
    @LastModifiedDate
    val updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    fun updateNickname(nickname: String): User {
        return this.copy(
            nickname = nickname
        )
    }

    fun updateProfile(description: String, tags: List<String>): User {
        return this.copy(
            description = description,
            tags = tags
        )
    }

    fun updateProfileImageUrl(profileImageUrl: String): User {
        return this.copy(profileImageUrl = profileImageUrl)
    }

    fun updateRefreshToken(refreshToken: String): User {
        return this.copy(
            refreshToken = refreshToken
        )
    }

    companion object {
        fun createBasicUser(email: String, password: String): User {
            return User(
                email = email,
                password = password,
                loginType = LoginType.BASIC
            )
        }
    }
}

enum class LoginType {
    BASIC, APPLE, KAKAO
}