package kr.mashup.bangwidae.asked.utils

object StringUtils {
    fun ellipsis(content: String, maxLength: Int): String {
        return if (content.length <= maxLength) content
        else "${content.substring(0, maxLength)}..."
    }
}