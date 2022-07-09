package kr.mashup.bangwidae.asked.utils

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Component
class S3ImageUtil(
    private val amazonS3: AmazonS3,
    @Value("\${cloud.aws.s3.bucket}") private val bucket: String
) {
    fun upload(file: MultipartFile, dirName: String): String {
        val fileName = "$dirName/${UUID.randomUUID()}${file.name}"
        amazonS3.putObject(
            PutObjectRequest(
                bucket,
                fileName,
                file.inputStream,
                getObjectMetadata(file.inputStream.available().toLong())
            ).withCannedAcl(CannedAccessControlList.PublicRead)
        )
        return amazonS3.getUrl(bucket, fileName).toString()
    }

    private fun getObjectMetadata(contentLength: Long): ObjectMetadata {
        return ObjectMetadata().apply { this.contentLength = contentLength }
    }
}