package kr.mashup.bangwidae.asked.config.encrypt

import com.ulisesbocchio.jasyptspringboot.encryptor.DefaultLazyEncryptor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * Run > Edit Configurations > Configuration > Environment variables > 'JASYPT_ENCRYPTOR_PASSWORD={암호화키}' 입력
 */
@Disabled("필요할 때만 사용하기 위해 disabled 처리함")
@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class EncryptConfigTest {
    @Value("\${jasypt.encryptor.password}")
    private val jasyptEncryptorPassword: String? = null

    @Autowired
    var configurableEnvironment: ConfigurableEnvironment? = null
    var encryptor: DefaultLazyEncryptor? = null
    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {
        println(jasyptEncryptorPassword)
        if (org.junit.platform.commons.util.StringUtils.isBlank(jasyptEncryptorPassword)) {
            throw Exception("jasypt.encryptor.password must not be null, empty or blank.")
        }
        encryptor = DefaultLazyEncryptor(configurableEnvironment)
    }

    @Test
    fun testForEncryption() {
        val source = "source"
        println("source: $source")
        println("encrypted: " + encryptor!!.encrypt(source))
    }

    @Test
    fun testForDecryption() {
        // 암호화 되지 않은 문자열을 넣으면 복호화시 에러 발생함
        val source = "string want to decrypt"
        println("source: $source")
        println("encrypted: " + encryptor!!.decrypt(source))
    }
}