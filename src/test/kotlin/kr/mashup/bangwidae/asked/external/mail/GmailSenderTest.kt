package kr.mashup.bangwidae.asked.external.mail

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Disabled("메일 보내보고 싶을 때 사용 해보기")
@SpringBootTest
class GmailSenderTest(
    @Autowired
    private val gmailSender: GmailSender
){

    @Test
    fun test() {
        val targetMail = "khjvvv7@gmail.com"
        val title = "제목"
        val text = "내용"
        gmailSender.send(targetMail, title, text)
    }

}