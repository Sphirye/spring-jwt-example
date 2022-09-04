package com.sphirye.jwtexample.config.exception

class BadCredentialsException: RuntimeException {
    constructor(message: String?): super(message)
    constructor(): super("BadCredentials")
}