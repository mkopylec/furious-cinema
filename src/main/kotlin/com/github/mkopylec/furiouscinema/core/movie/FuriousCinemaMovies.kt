package com.github.mkopylec.furiouscinema.core.movie

class FuriousCinemaMovies(
    private val properties: MovieProperties,
    private val movies: Movies
) {
    suspend fun loadMovies(): List<Movie> = movies.ofIds(properties.availableMovieIds)

    suspend fun loadMovies(ids: List<String>): List<Movie> = movies.ofIds(ids)

    suspend fun loadMovie(id: String): Movie {
        if (id !in properties.availableMovieIds) throw MovieNotFound
        return movies.ofId(id)
    }
}
