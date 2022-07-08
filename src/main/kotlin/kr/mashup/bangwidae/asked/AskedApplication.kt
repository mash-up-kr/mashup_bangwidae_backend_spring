package kr.mashup.bangwidae.asked

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class AskedApplication

fun main(args: Array<String>) {
    runApplication<AskedApplication>(*args)
}
//CODEOWNER 테스트