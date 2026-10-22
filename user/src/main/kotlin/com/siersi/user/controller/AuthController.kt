package com.siersi.user.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import com.siersi.common.utils.Result
import com.siersi.user.request.auth.LoginRequest
import com.siersi.user.request.auth.RegisterRequest
import com.siersi.user.service.user.UserService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService,
) {

    @PostMapping("/register")
    fun register(@RequestBody @Valid registerRequest: RegisterRequest): Result<Unit> {
        userService.register(registerRequest)
        return Result.success("注册成功")
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid loginRequest: LoginRequest): Result<String> {
        return Result.success("登录成功", userService.login(loginRequest))
    }
}