package com.siersi.gateway.config

import com.siersi.common.utils.Result
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import tools.jackson.databind.json.JsonMapper

@Configuration
class SecurityConfig(
    private val jsonMapper: JsonMapper,
) {
    private val log = LoggerFactory.getLogger("SecurityConfig")

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .logout { it.disable() }
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .authorizeExchange { auth ->
                auth
                    .pathMatchers("/api/user/auth/**").permitAll()
                    .pathMatchers("/api/user/test/**").permitAll()
                    .anyExchange().authenticated()
            }
            .exceptionHandling { ex ->
                ex.authenticationEntryPoint(authenticationEntryPoint())
                ex.accessDeniedHandler(accessDeniedHandler())
            }
            .build()
    }

    private fun authenticationEntryPoint(): ServerAuthenticationEntryPoint =
        ServerAuthenticationEntryPoint { exchange, ex ->
            log.info("未认证 path={} reason={}", exchange.request.path.value(), ex.javaClass.simpleName)
            write(exchange, Result.failure("请登录", 401))
        }

    private fun accessDeniedHandler(): ServerAccessDeniedHandler =
        ServerAccessDeniedHandler { exchange, ex ->
            log.info("无权限 path={} reason={}", exchange.request.path.value(), ex.javaClass.simpleName)
            write(exchange, Result.failure("无权限访问", 403))
        }

    private fun write(exchange: ServerWebExchange, result: Any): Mono<Void> {
        val response = exchange.response
        response.statusCode = HttpStatus.OK
        response.headers.contentType = MediaType.APPLICATION_JSON

        val body = jsonMapper.writeValueAsString(result)
        return response.writeWith(
            Mono.just(response.bufferFactory().wrap(body.toByteArray(Charsets.UTF_8)))
        )
    }
}
