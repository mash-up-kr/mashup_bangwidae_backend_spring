import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.3"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "kr.mashup.bangwidae"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

val swaggerVersion: String by project
val javaJwtVersion: String by project
val kotlinLoggingVersion: String by project
val jacksonKotlinModuleVersion: String by project
val embeddedMongoVersion: String by project
val jasyptVersion: String by project
val apacheCommonsVersion: String by project
val kotestVersion: String by project
val mockkVersion: String by project

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

	implementation("com.auth0:java-jwt:$javaJwtVersion")
	implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:${jasyptVersion}")
	implementation("io.github.microutils:kotlin-logging-jvm:${kotlinLoggingVersion}")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	implementation("io.springfox:springfox-boot-starter:${swaggerVersion}")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonKotlinModuleVersion}")
	implementation("org.apache.commons:commons-lang3:${apacheCommonsVersion}")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:${embeddedMongoVersion}")
	testImplementation("io.kotest:kotest-runner-junit5:${kotestVersion}")
	testImplementation("io.kotest:kotest-assertions-core:${kotestVersion}")
	testImplementation("io.mockk:mockk:${mockkVersion}")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
