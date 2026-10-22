package com.siersi.user.service.user

import com.mybatisflex.core.query.QueryWrapper
import com.mybatisflex.spring.service.impl.ServiceImpl
import com.siersi.common.utils.JwtUtil
import com.siersi.user.entity.User
import com.siersi.user.exception.BusinessException
import com.siersi.user.mapper.UserMapper
import com.siersi.user.request.auth.RegisterRequest
import org.springframework.stereotype.Service
import com.siersi.user.entity.table.UserTableDef.USER
import com.siersi.user.request.auth.LoginRequest
import org.springframework.security.crypto.password.PasswordEncoder

@Service
class UserServiceImpl(
    private val userMapper: UserMapper,
    private val jwtUtil: JwtUtil,
    private val passwordEncoder: PasswordEncoder,
): ServiceImpl<UserMapper, User>(), UserService {

    override fun register(registerRequest: RegisterRequest): Unit {
        val queryWrapper = QueryWrapper.create()
            .where(USER.ACCOUNT.eq(registerRequest.account))

        userMapper.selectOneByQuery(queryWrapper)?.let {
            throw BusinessException("用户已存在")
        }

        User(
            account = registerRequest.account,
            password = passwordEncoder.encode(registerRequest.password)!!,
        ).also { userMapper.insertSelective(it) }
    }

    override fun login(loginRequest: LoginRequest): String {
        val queryWrapper = QueryWrapper.create()
            .where(USER.ACCOUNT.eq(loginRequest.account))

        val user: User = userMapper.selectOneByQuery(queryWrapper) ?: throw BusinessException("账号或密码错误")

        if (!passwordEncoder.matches(loginRequest.password, user.password)) throw BusinessException("账号或密码错误")

        return jwtUtil.generateToken(user.id ?: throw BusinessException("用户id不存在"))
    }
}