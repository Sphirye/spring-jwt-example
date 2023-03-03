package com.sphirye.jwtexample.controller

import com.google.gson.Gson
import com.sphirye.jwtexample.entity.model.CustomUserDetails
import com.sphirye.jwtexample.entity.model.LoginResponse
import com.sphirye.jwtexample.repository.UserRepository
import com.sphirye.jwtexample.security.SessionManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AccessTestingController {

    @Autowired lateinit var sessionManager: SessionManager

    @GetMapping("/public/test")
    fun public(): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.OK).body("Welcome, this is a public route")
    }

    @GetMapping("/api/test")
    fun secure(): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.OK).body("Welcome, this is a secure route")
    }

    @GetMapping("/api/mod-test")
    @PreAuthorize("hasAuthority('MOD')")
    fun mod(): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.OK).body("Welcome, this is a private route restricted to mods")
    }

    @GetMapping("/api/admin-test")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun admin(): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.OK).body("Welcome, this is a private route restricted to admins")
    }

    @GetMapping("/api/multi-test")
    @PreAuthorize("hasAnyAuthority('MOD', 'ADMIN')")
    fun multi(): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.OK).body("Welcome, this is a private route multi-restricted to mods and admins")
    }
}