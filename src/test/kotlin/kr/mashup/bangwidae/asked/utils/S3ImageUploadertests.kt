package kr.mashup.bangwidae.asked.utils

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile

//@Disabled("필요할 때만 사용하기 위해 disabled 처리함")
@SpringBootTest
class S3ImageUploadertests(
    @Autowired
    val s3ImageUploader: S3ImageUtil
) {
    @Test
    @DisplayName("s3Upload test")
    fun s3UploadTest() {
        //given
        val imageFiles = MockMultipartFile("imageFiles", ByteArray(1))
        //when
        val url = s3ImageUploader.upload(imageFiles, UploadDirName.PROFILE)
        println(url)
    }
}