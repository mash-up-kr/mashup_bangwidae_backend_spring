package kr.mashup.bangwidae.asked.model

object MailTemplate {
    fun createCertTemplate(number: String) =
        """
            <!DOCTYPE html> 
            
            <body text-align=centre><a herf='https://apidocs.mailchimp.com/api/2.0/campaigns/create.php'>
          
            인증번호 : $number
            
            </body> </html>
        """.trimIndent()
}
