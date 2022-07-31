package kr.mashup.bangwidae.asked.exception

enum class DoriDoriExceptionType(
    val message: String,
) {
    // COMMON
    COMMON_ERROR("문제가 발생했어요"),
    NOT_EXIST("It does not exist."),
    FAIL("Internal server error."),
    NOT_ALLOWED_TO_ACCESS("접근 권한이 없어요."),

    // AUTH
    TOKEN_EXPIRED("토큰이 만료되었어요"),
    AUTHENTICATED_FAIL("인증에 실패 했어요"),
    PERMISSION_DENIED("권한이 없어요"),
    DUPLICATED_USER("이미 가입된 이메일 이에요"),
    CERTIFICATE_FAILED("인증번호가 일치하지 않아요"),
    NOT_CERTIFICATED_EMAIL("이메일 인증이 되지 않았어요"),
    LOGIN_FAILED("로그인에 실패했어요"),
    INVALID_PASSWORD("비밀번호가 틀렸어요"),
    INVALID_PASSWORD_REGEX("비밀번호는 숫자, 영어, 특수문자로 이루어져야 해요"),
    INVALID_PASSWORD_LENGTH("비밀번호는 8자리 이상이어야 해요"),

    // USER
    DUPLICATED_NICKNAME("이미 존재하는 닉네임이에요"),
    USER_NOT_FOUND("유저를 찾을 수 없어요"),

    // PLACE
    INVALID_COUNTRY("한국에서만 위치 기능을 사용할 수 있어요"),
    PLACE_FETCH_FAIL("위치 정보를 받아오지 못했어요"),
    REPRESENTATIVE_ADDRESS_NOT_EXIST("대표 주소를 찾지 못했어요"),

    // POST
    POST_NOT_ALLOWED_FOR_USER("유저가 해당 포스트에 대한 권한이 없어요"),
    POST_WRITER_USER_NOT_EXIST("포스트에 대한 작성자를 찾을 수 없어요"),

    // WARD
    WARD_NOT_FOUND("와드를 찾을 수 없어요"),
    WARD_MAX_COUNT("더 이상 와드를 심을 수 없어요"),
}