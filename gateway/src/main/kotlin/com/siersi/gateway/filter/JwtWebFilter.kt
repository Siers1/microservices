package com.siersi.gateway.filter

import com.siersi.common.utils.JwtUtil
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class JwtWebFilter(
    private val jwtUtil: JwtUtil
) : WebFilter, Ordered {

    private val log = LoggerFactory.getLogger("JwtWebFilter")

    override fun getOrder(): Int = ORDER

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val header = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)

        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            return chain.filter(exchange)
        }

        val token = header.substring(BEARER_PREFIX.length)

        val authentication = try {
            if (!jwtUtil.verifyToken(token)) {
                log.info("token 无效，按匿名处理，交给授权层拦截")
                null
            } else {
                val userId = jwtUtil.getUserId(token)
                log.info("校验通过 userId={}", userId)
                UsernamePasswordAuthenticationToken(userId, null, emptyList())
            }
        } catch (e: Exception) {
            log.info("解析异常 {}: {}，按匿名处理", e.javaClass.simpleName, e.message)
            null
        }

        if (authentication == null) {
            return chain.filter(exchange)
        }

        val mutated = exchange.mutate()
            .request(
                exchange.request.mutate()
                    .header(USER_ID_HEADER, authentication.principal.toString())
                    .build()
            )
            .build()

        return chain.filter(mutated)
            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
    }

    companion object {
        private const val BEARER_PREFIX = "Bearer "
        const val USER_ID_HEADER = "X-User-Id"
        private const val ORDER = -200
    }
}
