package kr.mashup.bangwidae.asked.exception

import org.springframework.http.HttpStatus

enum class DoriDoriExceptionType(
    val httpStatus: HttpStatus,
    val code: String,
    val message: String,
) {
    // COMMON
    COMMON_ERROR(
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
        code = Code.COMMON_ERROR,
        message = "문제가 발생했어요"
    ),
    NOT_EXIST(
        httpStatus = HttpStatus.NOT_FOUND,
        code = Code.NOT_EXIST,
        message = "It does not exist."
    ),
    FAIL(
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
        code = Code.FAIL,
        message = "Internal server error."
    ),

    // AUTH
    LOGIN_FAILED(
        httpStatus = HttpStatus.UNAUTHORIZED,
        code = Code.LOGIN_FAILED,
        message = "로그인에 실패했어요"
    ),
    INVALID_PASSWORD(
        httpStatus = HttpStatus.BAD_REQUEST,
        code = Code.INVALID_PASSWORD,
        message = "비밀번호가 틀렸어요"
    ),
    INVALID_PASSWORD_REGEX(
        httpStatus = HttpStatus.BAD_REQUEST,
        code = Code.INVALID_PASSWORD_REGEX,
        message = "비밀번호는 숫자, 영어, 특수문자로 이루어져야 해요"
    ),
    INVALID_PASSWORD_LENGTH(
        httpStatus = HttpStatus.BAD_REQUEST,
        code = Code.INVALID_PASSWORD_LENGTH,
        message = "비밀번호는 8자리 이상이어야 해요"
    ),

    // PLACE
    INVALID_COUNTRY(
        httpStatus = HttpStatus.BAD_REQUEST,
        code = Code.INVALID_COUNTRY,
        message = "한국에서만 위치 기능을 사용할 수 있어요"
    ),
    PLACE_FETCH_FAIL(
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
        code = Code.PLACE_FETCH_FAIL,
        message = "위치 정보를 받아오지 못했어요"
    ),
    ;

    object Code {
        const val COMMON_ERROR = "COMMON_ERROR"
        const val NOT_EXIST = "EXAMPLE_NOT_EXIST_ERROR_CODE"
        const val FAIL = "EXAMPLE_FAIL_ERROR_CODE"
        const val LOGIN_FAILED = "LOGIN_FAILED"
        const val INVALID_PASSWORD = "INVALID_PASSWORD"
        const val INVALID_PASSWORD_REGEX = "INVALID_PASSWORD_REGEX"
        const val INVALID_PASSWORD_LENGTH = "INVALID_PASSWORD_LENGTH"

        const val INVALID_COUNTRY = "INVALID_COUNTRY"
        const val PLACE_FETCH_FAIL = "PLACE_FETCH_FAIL"
    }
}