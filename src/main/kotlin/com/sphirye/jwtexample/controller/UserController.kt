package com.sphirye.jwtexample.controller

import com.sphirye.jwtexample.entity.User
import com.sphirye.jwtexample.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) {
    @PostMapping("/public/user/sign-up")
    fun signUp(
        @RequestParam("username") username: String,
        @RequestParam("password") password: String,
        @RequestParam("role") role: String
    ): ResponseEntity<User> {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(username, password, role))
    }

    @PatchMapping("/api/user/{id}/activate")
    fun setStatus(
        @RequestParam("activated") activated: Boolean,
        @PathVariable id: Long
    ): ResponseEntity<User> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.setStatus(id, activated))
    }
}