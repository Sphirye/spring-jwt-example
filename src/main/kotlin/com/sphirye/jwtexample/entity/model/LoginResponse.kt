package com.sphirye.jwtexample.entity.model

import com.sphirye.jwtexample.entity.User

data class LoginResponse(
    var token: String? = null,
    var user: User? = null
)