package com.github.mkopylec.furiouscinema.infrastructure.persistence

import com.github.mkopylec.furiouscinema.core.authentication.Authentication
import com.github.mkopylec.furiouscinema.core.authentication.Authentications
import com.github.mkopylec.furiouscinema.core.authentication.Moviegoer
import com.github.mkopylec.furiouscinema.core.authentication.Owner
import org.springframework.stereotype.Repository

@Repository
class InMemoryAuthentications : Authentications {

    private val authentications: List<Authentication> = listOf(
        Authentication("moviegoer-authentication-token-1", 100000, Moviegoer),
        Authentication("moviegoer-authentication-token-2", 200000, Moviegoer),
        Authentication("owner-authentication-token", 300000, Owner)
    )

    override fun forToken(token: String): Authentication? = authentications.find { it.token == token }
}
