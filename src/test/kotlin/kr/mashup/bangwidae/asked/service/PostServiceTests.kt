package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.mockGangnamPost
import kr.mashup.bangwidae.asked.mockSinnonhyeonPost
import kr.mashup.bangwidae.asked.model.post.Post
import kr.mashup.bangwidae.asked.repository.PostRepository
import kr.mashup.bangwidae.asked.repository.UserRepository
import kr.mashup.bangwidae.asked.service.place.PlaceService
import kr.mashup.bangwidae.asked.service.post.PostService
import kr.mashup.bangwidae.asked.utils.getLatitude
import kr.mashup.bangwidae.asked.utils.getLongitude
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType
import org.springframework.data.mongodb.core.index.GeospatialIndex
import org.springframework.data.mongodb.core.indexOps

@DataMongoTest
class PostServiceTests(
    @Autowired
    private val mongoTemplate: MongoTemplate,
    @Autowired
    private val postRepository: PostRepository,
    @Autowired
    private val placeService: PlaceService,
    @Autowired
    private val userRepository: UserRepository
) {
    private val postService = PostService(postRepository, placeService, userRepository)

    @Test
    @DisplayName("커서 테스트")
    fun findNearLocationCursorTest() {
        //given
        val gangnamPost = mockGangnamPost()
        val sinnonhyeonPost = mockSinnonhyeonPost()

        //when
        mongoTemplate.indexOps<Post>().ensureIndex(GeospatialIndex("location").typed(GeoSpatialIndexType.GEO_2DSPHERE))
        for (i in 0 until 100) {
            postRepository.save(sinnonhyeonPost.copy(content = i.toString()))
        }

        val nearPostsPage1 = postService.getNearPost(
            gangnamPost.location.getLongitude(),
            gangnamPost.location.getLatitude(),
            1000.0,
            null,
            30
        )
        val lastId = nearPostsPage1.values.last().id
        val nearPostsPage2 = postService.getNearPost(
            gangnamPost.location.getLongitude(),
            gangnamPost.location.getLatitude(),
            1000.0,
            ObjectId(lastId),
            30
        )
        val contentContrastPage1List = (99 downTo 70).map { it.toString() }
        val postContentPage1List = nearPostsPage1.values.map { it.content }
        val contentContrastPage2List = (69 downTo 40).map { it.toString() }
        val postContentPage2List = nearPostsPage2.values.map { it.content }

        //then
        assertThat(postContentPage1List).isEqualTo(contentContrastPage1List)
        assertThat(postContentPage2List).isEqualTo(contentContrastPage2List)
    }
}