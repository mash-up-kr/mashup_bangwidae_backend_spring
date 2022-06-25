package kr.mashup.bangwidae.asked.config

import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory

@Configuration
@EnableMongoAuditing
class MongoConfig {
	@Bean
	@ConfigurationProperties("spring.data.mongodb")
	fun properties(): MongoProperties {
		return MongoProperties()
	}

	@Bean
	fun mongoFactory(properties: MongoProperties): MongoDatabaseFactory {
		return SimpleMongoClientDatabaseFactory(properties.uri)
	}

	@Bean
	fun mongoTemplate(mongoFactory: MongoDatabaseFactory): MongoTemplate {
		return MongoTemplate(mongoFactory)
	}

	@Bean
	fun transactionManager(mongoFactory: MongoDatabaseFactory): MongoTransactionManager {
		return MongoTransactionManager(mongoFactory)
	}
}