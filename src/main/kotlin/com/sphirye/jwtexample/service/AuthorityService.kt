package com.sphirye.jwtexample.service

import com.sphirye.jwtexample.config.exception.DuplicatedException
import com.sphirye.jwtexample.config.exception.NotFoundException
import com.sphirye.jwtexample.entity.Authority
import com.sphirye.jwtexample.repository.AuthorityRepository
import org.springframework.stereotype.Service

@Service
class AuthorityService(
    private val authorityRepository: AuthorityRepository
) {
    fun init() {
        create("ADMIN")
        create("MOD")
        create("USER")
    }

    fun create(role: String): Authority {
        if (existsByRole(role)) { throw DuplicatedException("$role role already exists") }
        val authority = Authority(role)
        return authorityRepository.save(authority)
    }

    fun findByRole(role: String): Authority {
        if (!existsByRole(role)) { throw NotFoundException("$role role does not exists") }
        return authorityRepository.findByRole(role)
    }

    fun existsByRole(role: String): Boolean {
        return authorityRepository.existsByRole(role)
    }
}