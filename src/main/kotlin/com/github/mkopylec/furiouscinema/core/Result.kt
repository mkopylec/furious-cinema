package com.github.mkopylec.furiouscinema.core

sealed class Result<V, T>(
    open val value: T? = null,
    open val violation: V? = null
) where V : Violation, V : Enum<*>

interface Violation
