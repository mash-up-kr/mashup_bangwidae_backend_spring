package kr.mashup.bangwidae.asked.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.bson.types.ObjectId
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class ObjectMapperConfig {
	@Bean
	@Primary
	fun objectMapper(): ObjectMapper {
		val objectIdModule = SimpleModule().apply { addSerializer(ObjectId::class.java, ToStringSerializer()) }
		return ObjectMapper().registerModule(objectIdModule)
	}
}