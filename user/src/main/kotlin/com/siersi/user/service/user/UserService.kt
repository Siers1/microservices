package com.siersi.user.service.user

import com.mybatisflex.core.service.IService
import com.siersi.user.entity.User
import com.siersi.user.request.auth.LoginRequest
import com.siersi.user.request.auth.RegisterRequest

interface UserService: IService<User> {
    fun register(registerRequest: RegisterRequest): Unit

    fun login(loginRequest: LoginRequest): String
}