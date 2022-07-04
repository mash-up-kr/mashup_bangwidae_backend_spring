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
    DUPLICATED_USER(
        httpStatus = HttpStatus.BAD_REQUEST,
        code = Code.DUPLICATED_USER,
        message = "이미 가입된 이메일 이에요"
    ),
    CERTIFICATE_FAILED(
        httpStatus = HttpStatus.BAD_REQUEST,
        code = Code.CERTIFICATE_FAILED,
        message = "인증번호가 일치하지 않아요"
    ),
    NOT_CERTIFICATED_EMAIL(
        httpStatus = HttpStatus.BAD_REQUEST,
        code = Code.NOT_CERTIFICATED_EMAIL,
        message = "이메일 인증이 되지 않았어요"
    ),
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
    REPRESENTATIVE_ADDRESS_NOT_EXIST(
        httpStatus = HttpStatus.BAD_REQUEST,
        code = Code.REPRESENTATIVE_ADDRESS_NOT_EXIST,
        message = "대표 주소를 찾지 못했어요"
    ),

    // POST
    POST_NOT_ALLOWED_FOR_USER(
        httpStatus = HttpStatus.UNAUTHORIZED,
        code = Code.POST_NOT_ALLOWED_FOR_USER,
        message = "유저가 해당 포스트에 대한 권한이 없어요"
    )
    ;

    object Code {
        const val COMMON_ERROR = "COMMON_ERROR"
        const val NOT_EXIST = "EXAMPLE_NOT_EXIST_ERROR_CODE"
        const val FAIL = "EXAMPLE_FAIL_ERROR_CODE"
        const val DUPLICATED_USER = "DUPLICATED_USER"
        const val CERTIFICATE_FAILED = "CERTIFICATE_FAILED"
        const val NOT_CERTIFICATED_EMAIL = "NOT_CERTIFICATED_EMAIL"
        const val LOGIN_FAILED = "LOGIN_FAILED"
        const val INVALID_PASSWORD = "INVALID_PASSWORD"
        const val INVALID_PASSWORD_REGEX = "INVALID_PASSWORD_REGEX"
        const val INVALID_PASSWORD_LENGTH = "INVALID_PASSWORD_LENGTH"

        const val INVALID_COUNTRY = "INVALID_COUNTRY"
        const val PLACE_FETCH_FAIL = "PLACE_FETCH_FAIL"
        const val REPRESENTATIVE_ADDRESS_NOT_EXIST = "REPRESENTATIVE_ADDRESS_NOT_EXIST"

        const val POST_NOT_ALLOWED_FOR_USER = "POST_NOT_ALLOWED_FOR_USER"
    }
}