package kr.mashup.bangwidae.asked

import kr.mashup.bangwidae.asked.model.document.LoginType
import kr.mashup.bangwidae.asked.model.document.User
import kr.mashup.bangwidae.asked.model.document.post.Post
import kr.mashup.bangwidae.asked.utils.GeoUtils
import org.bson.types.ObjectId

fun mockUser(): User {
    return User(
        id = null,
        nickname = "mock user nickname",
        email = "mock user loginId",
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
        region = null
    )
}

fun mockGangnamPost(): Post {
    return Post(
        id = null,
        userId = ObjectId(),
        content = "강남역 입니다.",
        location = GeoUtils.geoJsonPoint(127.027926, 37.497175),
        representativeAddress = "강남",
        region = null
    )
}

fun mockNonhyeonPost(): Post {
    return Post(
        id = null,
        userId = ObjectId(),
        content = "논현역 입니다.",
        location = GeoUtils.geoJsonPoint(127.02158470345, 37.511130469556),
        representativeAddress = "논현",
        region = null
    )
}