package kr.mashup.bangwidae.asked

import kr.mashup.bangwidae.asked.model.post.Post
import kr.mashup.bangwidae.asked.repository.PostRepository
import kr.mashup.bangwidae.asked.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType
import org.springframework.data.mongodb.core.index.GeospatialIndex
import org.springframework.data.mongodb.core.indexOps
import org.springframework.data.repository.findByIdOrNull


@DataMongoTest
class MongoDbSpringIntegrationTest(
	@Autowired
	private val mongoTemplate: MongoTemplate,
	@Autowired
	private val userRepository: UserRepository,
	@Autowired
	private val postRepository: PostRepository
) {
	@Test
	@DisplayName("유저 저장하고 id로 찾기 몽고 테스트")
	fun saveAndFindUserTest() {
		// given
		val user = mockUser()

		// when
		val savedUser = userRepository.save(user)
		val foundUser = userRepository.findByIdOrNull(savedUser.id) ?: throw RuntimeException("mongo user test failed")

		// then
		assertTrue(savedUser.id == foundUser.id)
		assertTrue(savedUser.email == foundUser.email)
		assertTrue(savedUser.nickname == foundUser.nickname)
		assertTrue(savedUser.password == foundUser.password)
		assertTrue(savedUser.tags == foundUser.tags)
		assertTrue(savedUser.loginType == foundUser.loginType)
		assertTrue(savedUser.providerId == foundUser.providerId)
	}

	@Test
	@DisplayName("강남역에서 신논현역(800m), 논현역(1km 이상) 거리 테스트")
	fun findNearLocationPostTest() {
		//given
		val gangnamPost = mockGangnamPost()
		val sinnonhyeonPost = mockSinnonhyeonPost()
		val nonhyeonPost = mockNonhyeonPost()

		//when
		mongoTemplate.indexOps<Post>().ensureIndex(GeospatialIndex("location").typed(GeoSpatialIndexType.GEO_2DSPHERE))
		val savedSinnonhyeonPost = postRepository.save(sinnonhyeonPost)
		val savedNonhyeonPost = postRepository.save(nonhyeonPost)
		val nearGangnamPostIdList =
			postRepository.findByLocationNear(
				gangnamPost.location,
				Distance(1.0, Metrics.KILOMETERS)
			).map { it.id }

		//then
		assertThat(nearGangnamPostIdList).contains(savedSinnonhyeonPost.id)
		assertThat(nearGangnamPostIdList).doesNotContain(savedNonhyeonPost.id)
	}
}