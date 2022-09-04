package com.sphirye.jwtexample.config

import com.sphirye.jwtexample.config.exception.*
import org.hibernate.exception.ConstraintViolationException
import org.springframework.context.annotation.Configuration
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import java.time.format.DateTimeParseException

@Configuration
@ControllerAdvice(annotations = [RestController::class])
class ExceptionConfig {


    @ExceptionHandler(
        BadRequestException::class,
        DataIntegrityViolationException::class,
        IllegalArgumentException::class,
        ConstraintViolationException::class,
        DateTimeParseException::class,
        MethodArgumentNotValidException::class
    )
    fun badRequestException(e: Exception): ResponseEntity<*>? {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body<Any>(MessageError(e.message))
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun unauthorizedException(e: Exception): ResponseEntity<*>? {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body<Any>(MessageError(e.message))
    }

    @ExceptionHandler(NotFoundException::class)
    fun notFoundException(e: Exception): ResponseEntity<*>? {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body<Any>(MessageError(e.message))
    }

    @ExceptionHandler(DuplicatedException::class)
    fun duplicatedException(e: Exception): ResponseEntity<*>? {
        return ResponseEntity.status(HttpStatus.CONFLICT).body<Any>(MessageError(e.message))
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun badCredentialsException(e: Exception): ResponseEntity<*>? {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body<Any>(MessageError(e.message))
    }

}