package com.github.mkopylec.furiouscinema.core

import com.github.mkopylec.furiouscinema.core.common.FuriousCinemaException
import com.github.mkopylec.furiouscinema.core.movie.FuriousCinemaMovies
import com.github.mkopylec.furiouscinema.core.movie.Movies
import org.springframework.stereotype.Service
import java.math.BigDecimal.ZERO
import java.time.DayOfWeek

@Service
class FuriousCinema(
    properties: FuriousCinemaProperties,
    movies: Movies
) {
    private val cinemaMovies = FuriousCinemaMovies(properties.movie, movies)

    suspend fun loadMovies(): MoviesLoadingResult {
        val movies = cinemaMovies.loadMovies()
        return MoviesLoadingResult(movies.map { MovieSummary(it.id, it.title) })
    }

    suspend fun loadMovie(movieId: String): MovieLoadingResult = try {
        val movie = cinemaMovies.loadMovie(movieId)
        MovieLoadingResult(
            MovieDetails(
                movieId = movie.id,
                title = movie.title,
                description = movie.description,
                releaseDate = movie.releaseDate,
                moviegoersRating = ZERO, // TODO Set real rating after implementing voting
                imdbRating = movie.imdbRating,
                runtime = movie.runtime.value
            )
        )
    } catch (e: FuriousCinemaException) {
        MovieLoadingResult(enumValueOf<MovieLoadingViolation>(e.violation))
    }

    suspend fun vote(vote: NewVote): VotingResult {
        TODO()
    }

    suspend fun addRepertoire(repertoire: NewRepertoire): RepertoireAddingResult {
        TODO()
    }

    suspend fun addScreening(screening: NewScreening): ScreeningAddingResult {
        TODO()
    }

    suspend fun loadRepertoire(day: DayOfWeek): RepertoireLoadingResult {
        TODO()
    }
}
