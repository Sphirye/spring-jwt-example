package com.sphirye.jwtexample.security

import com.sphirye.jwtexample.entity.Authority
import com.sphirye.jwtexample.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Component("userDetailsService")
class CustomUserDetailsService : UserDetailsService {

    @Autowired lateinit var userRepository: UserRepository

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findOneWithAuthoritiesByUsername(username)
            .map { user: com.sphirye.jwtexample.entity.User -> createUser(username, user) }
            .orElseThrow { UsernameNotFoundException("$username -> not found in database.") }
    }

    private fun createUser(username: String, user: com.sphirye.jwtexample.entity.User): User {
        if (!user.isActivated) {
            throw RuntimeException("$username -> Is not activated.")
        }

        val grantedAuthorities = user.authorities!!.stream()
            .map { authority: Authority -> SimpleGrantedAuthority(authority.role) }
            .collect(Collectors.toList())

        return User(
            user.username,
            user.password,
            grantedAuthorities
        )
    }
}