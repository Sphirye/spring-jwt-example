package com.sphirye.jwtexample.config.exception

class UnauthorizedException: RuntimeException {
    constructor(message: String?): super(message)
    constructor(): super("Unauthorized")
}