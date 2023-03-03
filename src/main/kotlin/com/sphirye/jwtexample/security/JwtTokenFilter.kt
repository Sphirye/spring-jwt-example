package com.sphirye.jwtexample.security

import com.google.gson.Gson
import com.sphirye.jwtexample.entity.Authority
import com.sphirye.jwtexample.entity.model.CustomUserDetails
import com.sphirye.jwtexample.security.util.JwtTokenUtil
import com.sphirye.jwtexample.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.stream.Collectors
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtTokenFilter : OncePerRequestFilter() {

    @Autowired lateinit var jwtTokenUtil: JwtTokenUtil
    @Autowired lateinit var userDetailsService: UserDetailsService
    @Autowired lateinit var userService: UserService

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        // Get authorization header and validate
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (header == null || header.isEmpty() || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response)
            return
        }

        // Get jwt token and validate
        val token = header.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].trim { it <= ' ' }

        if (!jwtTokenUtil.validate(token)) {
            chain.doFilter(request, response)
            return
        }

        try {
            val jwtToken = jwtTokenUtil.resolveToken(request)
            if (jwtToken != null && jwtTokenUtil.validate(jwtToken)) {

                val username = jwtTokenUtil.getUsernameFromToken(jwtToken)
                val user = userService.findByUsername(username)
                val userDetails = userDetailsService.loadUserByUsername(username)

                val customUserDetails = CustomUserDetails(
                    username = userDetails.username,
                    password = userDetails.password,
                    authorities = userDetails.authorities,
                    id = user.id
                )

                val authentication = UsernamePasswordAuthenticationToken(customUserDetails, null, userDetails.authorities)

                // Establecer la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) { logger.error("Error al procesar la solicitud", e) }

        chain.doFilter(request, response)
    }
}