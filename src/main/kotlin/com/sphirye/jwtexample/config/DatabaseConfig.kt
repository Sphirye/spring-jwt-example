package com.sphirye.jwtexample.config

import com.sphirye.jwtexample.service.AuthorityService
import com.sphirye.jwtexample.service.UserService
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class DatabaseConfig(
    private val userService: UserService,
    private val authorityService: AuthorityService
) {

    @PostConstruct
    fun init() {
        authorityService.init()
        userService.init()
    }
}