package com.siersi.user

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@MapperScan(basePackages = ["com.siersi.user.mapper"])
class UserApplication

fun main(args: Array<String>) {
    runApplication<UserApplication>(*args)
}
