package kr.mashup.bangwidae.asked.service

import kr.mashup.bangwidae.asked.repository.PostRepository
import org.springframework.stereotype.Service

@Service
class PostService(
	private val postRepository: PostRepository
) {


}