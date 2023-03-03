package com.sphirye.jwtexample.security

import com.sphirye.jwtexample.entity.model.CustomUserDetails
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service

@Service
class SessionManager {

    fun getUser(): CustomUserDetails { return SecurityContextHolder.getContext().authentication.principal as CustomUserDetails }

}