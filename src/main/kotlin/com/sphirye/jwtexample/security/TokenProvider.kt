package com.sphirye.jwtexample.security

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.stream.Collectors

@Component
class TokenProvider(
    @param:Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.token-validity-in-seconds}") tokenValidityInSeconds: Long
) : InitializingBean {
    private val logger = LoggerFactory.getLogger(TokenProvider::class.java)
    private val tokenValidityInMilliseconds: Long
    private var key: Key? = null

    init {
        tokenValidityInMilliseconds = tokenValidityInSeconds * 1000
    }

    companion object {
        private const val AUTHORITIES_KEY = "auth"
    }

    override fun afterPropertiesSet() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))
    }

    fun createToken(authentication: Authentication): String {
        val authorities = authentication.authorities.stream()
            .map { obj: GrantedAuthority -> obj.authority }
            .collect(Collectors.joining(","))

        val validity = Date(Date().time + tokenValidityInMilliseconds)

        return Jwts.builder()
            .setSubject(authentication.name)
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact()
    }

    fun getAuthentication(token: String?): Authentication {
        val claims = Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body

        val authorities: Collection<GrantedAuthority> = Arrays
            .stream(claims[AUTHORITIES_KEY].toString().split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            .map { role: String? -> SimpleGrantedAuthority(role) }
            .collect(Collectors.toList())

        val principal = User(claims.subject, "", authorities)

        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    fun validateToken(token: String?): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: SecurityException) {
            logger.info("Invalid JWT signature.")
        } catch (e: MalformedJwtException) {
            logger.info("Invalid JWT signature.")
        } catch (e: ExpiredJwtException) {
            logger.info("Expired JWT token.")
        } catch (e: UnsupportedJwtException) {
            logger.info("Unsupported JWT token.")
        } catch (e: IllegalArgumentException) {
            logger.info("Invalid JWT token.")
        }
        return false
    }
}