package com.sphirye.jwtexample.entity.model

import com.sphirye.jwtexample.entity.User
import org.springframework.http.ResponseEntity

data class LoginResponse(
    var token: String? = null,
    var user: User? = null
)