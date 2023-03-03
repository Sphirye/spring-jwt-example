package com.sphirye.jwtexample.service

import com.google.gson.Gson
import com.sphirye.jwtexample.config.exception.BadRequestException
import com.sphirye.jwtexample.entity.User
import com.sphirye.jwtexample.security.CustomPasswordEncoder
import com.sphirye.jwtexample.security.util.JwtTokenUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService {

    @Autowired lateinit var userService: UserService
    @Autowired lateinit var authenticationManager: AuthenticationManager
    @Autowired lateinit var jwtTokenUtil: JwtTokenUtil
    @Autowired lateinit var passwordEncoder: PasswordEncoder

    fun login(username: String, password: String): String {

        val user = userService.findByUsername(username)
        if (!passwordEncoder.matches(password, user.password)) { throw BadRequestException("Bad credentials") }
        val authenticate = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
        return jwtTokenUtil.generateToken(authenticate)

    }

}