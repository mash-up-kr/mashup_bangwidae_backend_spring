package kr.mashup.bangwidae.asked.external.mail

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import javax.mail.Message
import javax.mail.internet.InternetAddress

@Service
class GmailSender(
    private val javaMailSender: JavaMailSender
){
    fun send(email: String, title: String, text: String) {
        val message = javaMailSender.createMimeMessage()
        message.addRecipient(Message.RecipientType.TO, InternetAddress(email))
        message.subject = title
        message.setText(text, "UTF-8", "html")
        javaMailSender.send(message)
    }
}

