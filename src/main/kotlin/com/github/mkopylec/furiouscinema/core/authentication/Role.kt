package com.github.mkopylec.furiouscinema.core.authentication

sealed class Role(
    val name: String
)

object Moviegoer : Role("MOVIEGOER")
object Owner : Role("OWNER")
