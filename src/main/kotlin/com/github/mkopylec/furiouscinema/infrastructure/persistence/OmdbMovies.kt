package com.github.mkopylec.furiouscinema.infrastructure.persistence

import com.github.mkopylec.furiouscinema.core.movie.Movie
import com.github.mkopylec.furiouscinema.core.movie.Movies
import com.github.mkopylec.furiouscinema.core.movie.Runtime
import com.github.mkopylec.furiouscinema.infrastructure.httpclient.OmdbHttpClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Repository

@Repository
class OmdbMovies(
    private val omdbClient: OmdbHttpClient
) : Movies {

    override suspend fun ofId(id: String): Movie = ofIds(listOf(id)).first()

    override suspend fun ofIds(ids: List<String>): List<Movie> = coroutineScope {
        ids.map { async { omdbClient.loadMovie(it) } }.awaitAll()
    }.map {
        try {
            Movie.fromPersistentState(
                id = it.imdbID,
                title = it.Title,
                description = it.Plot,
                releaseDate = it.Released,
                imdbRating = it.imdbRating,
                runtime = Runtime(it.Runtime)
            )
        } catch (e: Exception) {
            throw IllegalStateException("Error creating ${Movie::class} from persistent state", e)
        }
    }
}
