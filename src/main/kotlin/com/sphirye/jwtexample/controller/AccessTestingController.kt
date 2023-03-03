package com.sphirye.jwtexample.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AccessTestingController {

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