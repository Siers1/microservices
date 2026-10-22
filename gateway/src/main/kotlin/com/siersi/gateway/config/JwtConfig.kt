package com.siersi.gateway.config

import com.siersi.common.utils.JwtUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig {

    @Value($$"${jwt.secret}")
    private var secret: String = ""

    @Bean
    fun jwtUtil(): JwtUtil {
        return JwtUtil(secret)
    }
}
