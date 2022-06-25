package kr.mashup.bangwidae.asked.external.mail

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class GmailSenderTest(
    @Autowired
    private val gmailSender: GmailSender
){

    @Test
    fun test() {
        gmailSender.send()
    }

}