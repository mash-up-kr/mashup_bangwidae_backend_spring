package kr.mashup.bangwidae.asked.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider
import springfox.documentation.spring.web.readers.operation.HandlerMethodResolver
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.*
import javax.servlet.ServletContext
import kotlin.streams.toList

@EnableSwagger2
@Configuration
class SwaggerConfig {
    @Bean
    fun apiDocket(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .groupName("-> Api Document")
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.ant("/api/**"))
            .build()
    }
}