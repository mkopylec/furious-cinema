package com.github.mkopylec.furiouscinema.core.common

abstract class FuriousCinemaException(
    val violation: String,
    cause: Throwable? = null
) : Exception(violation, cause)
