package kr.mashup.bangwidae.asked

import kr.mashup.bangwidae.asked.model.LoginType
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.post.Post
import org.springframework.data.mongodb.core.geo.GeoJsonPoint

fun mockUser(): User {
	return User(
		id = null,
		nickname = "mock user nickname",
		loginId = "mock user loginId",
		password = "mock user password",
		providerId = "mock user providerId",
		loginType = LoginType.BASIC,
		tags = listOf("tag1", "tag2")
	)
}

fun mockSinnonhyeonPost(): Post {
	return Post(
		id = null,
		content = "신논현 교보문고 입니다.",
		location = GeoJsonPoint(127.024099, 37.504030)
	)
}

fun mockGangnamPost(): Post {
	return Post(
		id = null,
		content = "강남역 입니다.",
		location = GeoJsonPoint(127.027926, 37.497175)
	)
}

fun mockNonhyeonPost(): Post {
	return Post(
		id = null,
		content = "논현역 입니다.",
		location = GeoJsonPoint(127.02158470345, 37.511130469556)
	)
}