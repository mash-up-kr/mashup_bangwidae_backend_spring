package kr.mashup.bangwidae.asked.external.map

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(NaverMapProperties::class)
class NaverMapPropertiesConfiguration

@ConstructorBinding
@ConfigurationProperties(prefix = "map.naver")
data class NaverMapProperties(
    val authorization: Authorization,
    val host: String,
) {
    data class Authorization(
        val clientId: String,
        val clientSecret: String,
    )
}