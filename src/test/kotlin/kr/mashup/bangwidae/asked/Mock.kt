package kr.mashup.bangwidae.asked

import kr.mashup.bangwidae.asked.model.LoginType
import kr.mashup.bangwidae.asked.model.User
import kr.mashup.bangwidae.asked.model.post.Post
import kr.mashup.bangwidae.asked.utils.GeoUtils
import org.bson.types.ObjectId

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
		userId = ObjectId(),
		content = "신논현 교보문고 입니다.",
		location = GeoUtils.geoJsonPoint(127.024099, 37.504030),
		representativeAddress = "신논현",
		fullAddress = "서울특별시 강남구 봉은사로 지하102"
	)
}

fun mockGangnamPost(): Post {
	return Post(
		id = null,
		userId = ObjectId(),
		content = "강남역 입니다.",
		location = GeoUtils.geoJsonPoint(127.027926, 37.497175),
		representativeAddress = "강남",
		fullAddress = "서울특별시 강남구 강남대로66길 14"
	)
}

fun mockNonhyeonPost(): Post {
	return Post(
		id = null,
		userId = ObjectId(),
		content = "논현역 입니다.",
		location = GeoUtils.geoJsonPoint(127.02158470345, 37.511130469556),
		representativeAddress = "논현",
		fullAddress = "서울특별시 논현구 논현로77 14"
	)
}