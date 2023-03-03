package com.sphirye.jwtexample.controller

import com.sphirye.jwtexample.entity.model.LoginResponse
import com.sphirye.jwtexample.repository.UserRepository
import com.sphirye.jwtexample.service.AuthenticationService
import com.sphirye.jwtexample.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authenticationService: AuthenticationService,
    private val userService: UserService,
    private val userRepository: UserRepository
) {
    @PostMapping("/public/auth/login")
    fun login(@RequestParam username: String, @RequestParam password: String): ResponseEntity<LoginResponse> {
        val user = userService.findByUsername(username)
        return ResponseEntity.status(HttpStatus.OK).body(
            LoginResponse(
                token = authenticationService.login(username, password),
                user = user
            )
        )
    }
}