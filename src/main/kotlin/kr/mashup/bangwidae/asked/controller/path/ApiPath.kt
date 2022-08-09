package kr.mashup.bangwidae.asked.controller.path

object ApiPath {
    const val ROOT = "/api/v1"

    // Answer
    const val ANSWER_EDIT = "/answers/{answerId}"
    const val ANSWER_DELETE = "/answers/{answerId}"
    const val ANSWER_LIKE = "/answers/{answerId}/like"
    const val ANSWER_CANCEL_LIKE = "/answers/{answerId}/like"

    // Auth
    const val LOGIN = "/auth/login"
    const val CERT_MAIL_SEND = "/auth/mail/send"
    const val CERT_MAIL = "/auth/mail"
    const val ISSUE_TOKEN = "/auth/issue/token"

    // Comment
    const val COMMENT_EDIT = "/comments/{commentId}"
    const val COMMENT_DELETE = "/comments/{commentId}"
    const val COMMENT_LIKE = "/comments/{commentId}/like"
    const val COMMENT_CANCEL_LIKE = "/comments/{commentId}/like"

}