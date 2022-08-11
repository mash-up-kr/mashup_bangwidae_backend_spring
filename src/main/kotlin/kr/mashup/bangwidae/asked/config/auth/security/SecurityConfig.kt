package kr.mashup.bangwidae.asked.config.auth.security

import com.fasterxml.jackson.databind.ObjectMapper
import kr.mashup.bangwidae.asked.config.auth.jwt.JwtService
import kr.mashup.bangwidae.asked.config.auth.security.handler.AuthenticationExceptionHandler
import kr.mashup.bangwidae.asked.controller.dto.ApiErrorResponse
import kr.mashup.bangwidae.asked.controller.dto.ApiResponse
import kr.mashup.bangwidae.asked.exception.DoriDoriExceptionType
import kr.mashup.bangwidae.asked.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter


@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var jwtService: JwtService

    @Autowired
    lateinit var userService: UserService

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.antMatcher("/api/v1/**")
            .authorizeRequests()
            .antMatchers(
                "actuator/health",
                "/api/v1/user/join",
                "/api/v1/auth/issue/token",
                "/api/v1/auth/login",
                "/api/v1/place/reverse/geocode",
                "/api/v1/auth/mail/send",
                "/api/v1/auth/mail/cert",
                "/api/v1/questions/{questionId}",
                "/api/v1/questions/answered",
                "/api/v1/user/{userId}/link-share",
                "/api/v1/level-policy/{level}"
            ).permitAll()
            .antMatchers("/api/**").hasAuthority("ROLE")
        http.csrf().disable()
            .logout().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .requestCache().disable()
            .addFilterAt(tokenPreAuthFilter(), AbstractPreAuthenticatedProcessingFilter::class.java)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint())
            .accessDeniedHandler { request, response, accessDeniedException ->
                response.status = HttpStatus.FORBIDDEN.value()
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                objectMapper.writeValue(
                    response.outputStream,
                    ApiResponse.fail(
                        ApiErrorResponse(
                            code = DoriDoriExceptionType.PERMISSION_DENIED.name,
                            message = DoriDoriExceptionType.PERMISSION_DENIED.message
                        )
                    )
                )
            }

        return http.build()
    }

    @Bean
    fun tokenPreAuthFilter(): TokenPreAuthFilter {
        val tokenPreAuthFilter = TokenPreAuthFilter(jwtService, userService)
        tokenPreAuthFilter.setAuthenticationManager(ProviderManager(preAuthTokenProvider()))
        return tokenPreAuthFilter
    }

    @Bean
    fun preAuthTokenProvider(): PreAuthTokenProvider {
        return PreAuthTokenProvider()
    }

    @Bean
    fun authenticationEntryPoint(): AuthenticationEntryPoint {
        return AuthenticationExceptionHandler(objectMapper)
    }
}