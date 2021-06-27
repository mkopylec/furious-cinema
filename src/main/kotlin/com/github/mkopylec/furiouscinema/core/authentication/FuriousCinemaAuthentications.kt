package com.github.mkopylec.furiouscinema.core.authentication

class FuriousCinemaAuthentications(
    private val authentications: Authentications
) {
    fun authenticateMoviegoer(token: String): Authentication {
        val authentication = authentications.forToken(token) ?: throw NotAuthenticated
        if (authentication.role != Moviegoer) throw NotAMoviegoer
        return authentication
    }
}
