package com.sphirye.jwtexample.security

import com.google.gson.Gson
import com.sphirye.jwtexample.entity.Authority
import com.sphirye.jwtexample.entity.model.CustomUserDetails
import com.sphirye.jwtexample.entity.model.UserDetailModel
import com.sphirye.jwtexample.repository.UserRepository
import com.sphirye.jwtexample.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.stream.Collectors

class CustomAuthenticationManager : AuthenticationManager {

    @Autowired lateinit var userDetailsService: UserDetailsService
    @Autowired lateinit var passwordEncoder: PasswordEncoder
    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var userService: UserService

    override fun authenticate(authentication: Authentication): Authentication {

        val username = authentication.name.toString()
        val password = authentication.credentials.toString()

        if (username.isEmpty()) { throw BadCredentialsException("invalid login details") }

        val customUserDetails: CustomUserDetails

        try {
            val user = userService.findByUsername(username)
            if (!user.isActivated) { throw DisabledException("User is not activated") }

            val grantedAuthorities = user.authorities!!.stream()
                .map { authority: Authority -> SimpleGrantedAuthority(authority.role) }
                .collect(Collectors.toList())

            customUserDetails = CustomUserDetails(
                username = user.username,
                password = user.password,
                authorities = grantedAuthorities,
                id = user.id
            )

        } catch (exception: UsernameNotFoundException) {
            throw BadCredentialsException("invalid login details")
        }

        val successfulAuthentication = createSuccessfulAuthentication(authentication, customUserDetails)

        (successfulAuthentication as UsernamePasswordAuthenticationToken).details = customUserDetails.getId()

        SecurityContextHolder.getContext().authentication = successfulAuthentication

        return successfulAuthentication
    }

    private fun createSuccessfulAuthentication(authentication: Authentication, user: UserDetails): Authentication {
        val token = UsernamePasswordAuthenticationToken(user.username, authentication.credentials, user.authorities)
        token.details = authentication.details
        return token
    }
}