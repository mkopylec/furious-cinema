package com.github.mkopylec.furiouscinema.infrastructure.persistence

import com.github.mkopylec.furiouscinema.core.movie.Movie
import com.github.mkopylec.furiouscinema.core.movie.Movies
import com.github.mkopylec.furiouscinema.infrastructure.httpclient.OmdbHttpClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Repository
import java.time.Duration
import java.time.Duration.ofMinutes

@Repository
class OmdbMovies(
    private val omdbClient: OmdbHttpClient
) : Movies {

    override suspend fun ofIds(ids: List<String>): List<Movie> = coroutineScope {
        ids.map { async { omdbClient.loadMovie(it) } }.awaitAll()
    }.map {
        Movie.fromPersistentState(
            id = it.imdbID,
            title = it.Title,
            description = it.Plot,
            releaseDate = it.Released,
            imdbRating = it.imdbRating,
            runtime = it.Runtime.toDuration()
        )
    }

    private fun String.toDuration(): Duration {
        val parts = split(" ")
        if (parts[1] != "min") throw IllegalStateException("Invalid duration string: $this")
        return ofMinutes(parts[0].toLong())
    }
}
