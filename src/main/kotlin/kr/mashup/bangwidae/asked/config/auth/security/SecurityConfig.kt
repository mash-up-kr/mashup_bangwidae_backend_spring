package kr.mashup.bangwidae.asked.config.auth.security

import com.fasterxml.jackson.databind.ObjectMapper
import kr.mashup.bangwidae.asked.config.auth.jwt.JwtService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter


@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var jwtService: JwtService

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.antMatcher("/api/v1/**")
            .authorizeRequests()
            .antMatchers("actuator/health").permitAll()
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
            .authenticationEntryPoint { request, response, authException ->
                response.status = HttpStatus.UNAUTHORIZED.value()
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                objectMapper.writeValue(
                    response.outputStream,
                    "인증관련 에러 정의"
                )
            }
            .accessDeniedHandler { request, response, accessDeniedException ->
                response.status = HttpStatus.FORBIDDEN.value()
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                objectMapper.writeValue(
                    response.outputStream,
                    "권한관련 에러 정의"
                )
            }

        return http.build()
    }

    @Bean
    fun tokenPreAuthFilter(): TokenPreAuthFilter {
        val tokenPreAuthFilter = TokenPreAuthFilter()
        tokenPreAuthFilter.setAuthenticationManager(ProviderManager(preAuthTokenProvider()))
        return tokenPreAuthFilter
    }

    @Bean
    fun preAuthTokenProvider(): PreAuthTokenProvider {
        return PreAuthTokenProvider(
            jwtService
        )
    }
}