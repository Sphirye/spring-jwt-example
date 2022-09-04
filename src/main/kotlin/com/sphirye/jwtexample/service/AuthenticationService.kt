package com.sphirye.jwtexample.service

import com.sphirye.jwtexample.config.exception.BadRequestException
import com.sphirye.jwtexample.entity.User
import com.sphirye.jwtexample.entity.model.LoginResponse
import com.sphirye.jwtexample.security.CustomPasswordEncoder
import com.sphirye.jwtexample.security.JwtFilter
import com.sphirye.jwtexample.security.TokenProvider
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val tokenProvider: TokenProvider,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder
) {
    fun login(username: String, password: String): String {

        val authenticationToken = UsernamePasswordAuthenticationToken(username, password)
        val authentication: Authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken)
        SecurityContextHolder.getContext().authentication = authentication
        val jwt: String = tokenProvider.createToken(authentication)
        val httpHeaders = HttpHeaders()
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer $jwt")
        return jwt

    }

    fun matchPasswords(user: User, password: String) {
        if (!passwordEncoder.matches(password, user.password)) { throw BadRequestException("Bad credentials") }
    }

}