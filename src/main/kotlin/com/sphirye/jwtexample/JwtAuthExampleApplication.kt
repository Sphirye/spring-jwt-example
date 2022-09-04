package com.sphirye.jwtexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository

@SpringBootApplication
//@EnableJpaRepositories(includeFilters = [ComponentScan.Filter(Repository::class)])
class JwtAuthExampleApplication

fun main(args: Array<String>) {
	runApplication<JwtAuthExampleApplication>(*args)
}
