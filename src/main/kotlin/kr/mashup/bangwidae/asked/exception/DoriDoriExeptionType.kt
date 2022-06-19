package kr.mashup.bangwidae.asked.exception

import org.springframework.http.HttpStatus

enum class DoriDoriExceptionType(
    val httpStatus: HttpStatus,
    val code: String,
    val message: String,
) {
    NOT_EXIST(
        httpStatus = HttpStatus.NOT_FOUND,
        code = Code.NOT_EXIST,
        message = "It does not exist."
    ),
    FAIL(
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
        code = Code.FAIL,
        message = "Internal server error."
    );

    object Code {
        const val NOT_EXIST = "EXAMPLE_NOT_EXIST_ERROR_CODE"
        const val FAIL = "EXAMPLE_FAIL_ERROR_CODE"
    }
}