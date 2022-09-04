package com.sphirye.jwtexample.config.exception

class DuplicatedException: RuntimeException {
    constructor(message: String?): super(message)
    constructor(): super("Duplicated")
}