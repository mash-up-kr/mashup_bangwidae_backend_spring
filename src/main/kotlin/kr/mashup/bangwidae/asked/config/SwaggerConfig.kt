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

    // SpringBoot 2.6.X ~ + Swagger3 + actuator 조합일 때 뭔가 잘 안된다...
    // https://github.com/springfox/springfox/issues/3462
    @Bean
    fun webMvcRequestHandlerProvider(
        servletContext: Optional<ServletContext?>,
        methodResolver: HandlerMethodResolver?,
        handlerMappings: List<RequestMappingInfoHandlerMapping>
    ): WebMvcRequestHandlerProvider? {
        var handlerMappings = handlerMappings
        handlerMappings = handlerMappings.filter {
            it.javaClass.name.contains("RequestMapping")
        }.toList()
        return WebMvcRequestHandlerProvider(servletContext, methodResolver, handlerMappings)
    }
}