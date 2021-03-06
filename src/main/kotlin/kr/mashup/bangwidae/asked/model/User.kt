package kr.mashup.bangwidae.asked.model

import io.swagger.annotations.ApiModel
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("user")
data class User(
    @Id
    val id: ObjectId? = null,
    val nickname: String? = null,
    val email: String,
    val password: String? = null,
    val level: Int = 1,
    val providerId: String? = null,
    val loginType: LoginType,
    val description: String? = null,
    val tags: List<String> = emptyList(),
    val profileImageUrl: String? = null,
    val refreshToken: String? = null,

    @Version var version: Int? = null,
    @CreatedDate var createdAt: LocalDateTime? = null,
    @LastModifiedDate var updatedAt: LocalDateTime? = null
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

    fun getAnonymousUser(): User {
        return this.copy(nickname = "익명", profileImageUrl = DEFAULT_PROFILE_IMAGE_URL)
    }

    fun levelUp(): User {
        return this.copy(
            level = level + 1
        )
    }

    companion object {
        const val DEFAULT_PROFILE_IMAGE_URL: String =
            "https://dori-dori-bucket.kr.object.ncloudstorage.com/profile/da26c773-30f4-473e-989b-e2f3fdf825ff%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A9%E1%84%91%E1%85%B5%E1%86%AF.png"

        fun createBasicUser(email: String, password: String): User {
            return User(
                level = 1,
                email = email,
                password = password,
                loginType = LoginType.BASIC
            )
        }
    }
}

@ApiModel
enum class LoginType {
    BASIC, APPLE, KAKAO
}