package com.github.mkopylec.furiouscinema.core.movie

class FuriousCinemaMovies(
    private val properties: MovieProperties,
    private val movies: Movies
) {
    suspend fun loadMovies(): List<Movie> {
        return movies.ofIds(properties.availableMovieIds)
    }
}
