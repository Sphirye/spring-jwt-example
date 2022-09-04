package com.sphirye.jwtexample.config.exception

class BadRequestException: RuntimeException {
    constructor(message: String?): super(message)
    constructor(): super("BadRequest")
}