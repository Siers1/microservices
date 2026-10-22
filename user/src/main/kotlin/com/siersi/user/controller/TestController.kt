package com.siersi.user.controller

import com.siersi.common.utils.Result
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("/test")
    fun test(): Result<String> {
        return Result.success(data = "hello world!")
    }
}