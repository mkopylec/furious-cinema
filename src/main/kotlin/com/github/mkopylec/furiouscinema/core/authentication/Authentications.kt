package com.github.mkopylec.furiouscinema.core.authentication

interface Authentications {
    fun forToken(token: String): Authentication?
}
