package com.sphirye.jwtexample.security.util

import com.google.gson.Gson
import com.sphirye.jwtexample.entity.model.CustomUserDetails
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenUtil {

    @Autowired lateinit var userDetailsService: UserDetailsService

    @Value("\${jwt.secret}") private lateinit var jwtSecret: String
    @Value("\${jwt.token-validity-in-seconds}") private var jwtExpirationInSeconds: Long = 0
    @Value("\${spring.application.id}") lateinit var jwtIssuer: String

    private val logger: Logger? = LogManager.getLogger()
    private val signingKey: Key by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)) }

    fun generateToken(authentication: Authentication): String {
        val validity = Date(Date().time + jwtExpirationInSeconds)

        return Jwts.builder()
            .setSubject(authentication.principal.toString())
            .setIssuer(jwtIssuer)
            .setIssuedAt(Date())
            .setExpiration(validity) // 1 week
            .signWith(signingKey, SignatureAlgorithm.HS512)
            .compact()
    }

    fun getUsernameFromToken(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }

    fun getLol(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun validate(token: String): Boolean {
        try {
            Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)

            return true
        }  catch (e: SecurityException) { logger!!.error("Invalid JWT signature.")
        } catch (e: MalformedJwtException) { logger!!.error("Invalid JWT signature.")
        } catch (e: ExpiredJwtException) { logger!!.error("Expired JWT token.")
        } catch (e: UnsupportedJwtException) { logger!!.error("Unsupported JWT token.")
        } catch (e: IllegalArgumentException) { logger!!.error("Invalid JWT token.")
        }
        return false
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }
}