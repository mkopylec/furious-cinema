package com.github.mkopylec.furiouscinema.core.authentication

class FuriousCinemaAuthentications(
    private val authentications: Authentications
) {
    fun authenticateMoviegoer(token: String): Authentication = authenticate(token, Moviegoer) { throw NotAMoviegoer }

    fun authenticateOwner(token: String): Authentication = authenticate(token, Owner) { throw NotAnOwner }

    private fun authenticate(token: String, requiredRole: Role, onForbiddenRole: () -> Unit): Authentication {
        val authentication = authentications.forToken(token) ?: throw NotAuthenticated
        if (authentication.role != requiredRole) onForbiddenRole()
        return authentication
    }
}
