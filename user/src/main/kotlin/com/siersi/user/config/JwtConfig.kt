package com.siersi.user.config

import com.siersi.common.utils.JwtUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig {

    @Bean
    fun jwtUtil(
        @Value($$"${jwt.secret}") secret: String,
        @Value($$"${jwt.expiration}") expiration: Int,
    ): JwtUtil = JwtUtil(secret, expiration)
}
