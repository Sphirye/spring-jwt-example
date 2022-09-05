package com.sphirye.jwtexample.controller

import com.sphirye.jwtexample.config.exception.BadRequestException
import com.sphirye.jwtexample.entity.Authority
import com.sphirye.jwtexample.entity.User
import com.sphirye.jwtexample.entity.model.LoginResponse
import com.sphirye.jwtexample.repository.UserRepository
import com.sphirye.jwtexample.service.AuthenticationService
import com.sphirye.jwtexample.service.UserService
import org.apache.coyote.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException.BadRequest

@RestController
class AuthController(
    private val authenticationService: AuthenticationService,
    private val userService: UserService,
    private val userRepository: UserRepository
) {
    @PostMapping("/public/auth/login")
    fun login(@RequestParam username: String, @RequestParam password: String): ResponseEntity<LoginResponse> {
        val user = userService.findByUsername(username)
        authenticationService.matchPasswords(user, password)
        return ResponseEntity.status(HttpStatus.OK).body(
            LoginResponse(
                token = authenticationService.login(username, password),
                user = user
            )
        )
    }
}