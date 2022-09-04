package com.sphirye.jwtexample.repository

import com.sphirye.jwtexample.entity.User
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByUserId(userId: Long): User

    fun findByUsername(username: String): User

    fun existsByUsername(username: String): Boolean

    @EntityGraph(attributePaths = ["authorities"])
    fun findOneWithAuthoritiesByUsername(username: String): Optional<User>
}