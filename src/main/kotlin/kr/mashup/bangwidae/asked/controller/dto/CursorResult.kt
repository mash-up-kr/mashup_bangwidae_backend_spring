package kr.mashup.bangwidae.asked.controller.dto

data class CursorResult<T>(
	val values: List<T>,
	val hasNext: Boolean
)