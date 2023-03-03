package com.sphirye.jwtexample.service

import com.sphirye.jwtexample.config.exception.DuplicatedException
import com.sphirye.jwtexample.config.exception.NotFoundException
import com.sphirye.jwtexample.entity.User
import com.sphirye.jwtexample.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService {

    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var authorityService: AuthorityService
    @Autowired lateinit var passwordEncoder: PasswordEncoder

    fun init() {
        create("admin", "1234", "ADMIN")
        create("mod", "1234", "MOD")
        create("user", "1234", "USER")
    }

    @Transactional
    fun create(username: String, password: String, role: String): User {
        if (existsByUsername(username)) {
            throw DuplicatedException("User with $username username already exists")
        }

        val authority = authorityService.findByRole(role)

        val user = User(
            username = username,
            password = passwordEncoder.encode(password),
            authorities = mutableSetOf(authority),
            isActivated = true
        )

        return userRepository.save(user)
    }

    fun setStatus(id: Long, activated: Boolean): User {
        var user = findById(id)
        user.isActivated = false
        return userRepository.save(user)
    }

    fun findByUsername(username: String): User {
        if (!existsByUsername(username)) { throw NotFoundException("User $username username not found") }
        return userRepository.findByUsername(username)
    }

    fun findById(id: Long): User {
        if (!existsById(id)) { throw NotFoundException("User with $id id not found") }
        return userRepository.getReferenceById(id)
    }

    fun existsByUsername(username: String): Boolean { return userRepository.existsByUsername(username) }
    fun existsById(id: Long): Boolean { return userRepository.existsById(id) }
}