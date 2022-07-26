package kr.mashup.bangwidae.asked.controller.dto

import kotlin.math.min

data class CursorResult<T>(
    val values: List<T>,
    val hasNext: Boolean
) {
    companion object {
        fun <T> from(values: List<T>, requestedSize: Int): CursorResult<T> {
            return CursorResult(
                values = values.subList(0, min(values.size, requestedSize)),
                hasNext = values.size > requestedSize
            )
        }
    }
}