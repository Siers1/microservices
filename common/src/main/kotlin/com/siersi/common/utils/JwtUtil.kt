package com.siersi.common.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.JWTVerifier
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

class JwtUtil(
    secret: String,
    private val expiration: Int = 0,
) {
    private val algorithm: Algorithm = Algorithm.HMAC256(secret)
    private val verifier: JWTVerifier = JWT.require(algorithm).build()

    fun generateToken(id: Long): String =
        JWT.create()
            .withSubject(id.toString())
            .withExpiresAt(Date.from(Instant.now().plus(expiration.toLong(), ChronoUnit.DAYS)))
            .sign(algorithm)

    fun verifyToken(token: String): Boolean =
        try {
            verifier.verify(token)
            true
        } catch (_: JWTVerificationException) {
            false
        }

    fun getUserId(token: String): Long =
        JWT.decode(token).subject.toLong()
}
