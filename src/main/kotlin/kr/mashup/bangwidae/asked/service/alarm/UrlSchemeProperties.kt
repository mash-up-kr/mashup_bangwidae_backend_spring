package kr.mashup.bangwidae.asked.service.alarm

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(UrlSchemeProperties::class)
class UrlSchemePropertiesConfiguration

@ConstructorBinding
@ConfigurationProperties(prefix = "url-scheme")
data class UrlSchemeProperties(
    val questionDetail: String,
    val postDetail: String,
    val userLevelUp: String,
)