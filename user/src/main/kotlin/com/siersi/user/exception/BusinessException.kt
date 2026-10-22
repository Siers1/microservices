package com.siersi.user.exception

open class BusinessException(
    message: String,
    val code: Int = 500
): RuntimeException(message)