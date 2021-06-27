package com.github.mkopylec.furiouscinema.core

import com.github.mkopylec.furiouscinema.core.movie.FuriousCinemaMovies
import com.github.mkopylec.furiouscinema.core.movie.Movies
import org.springframework.stereotype.Service
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

    suspend fun loadMovie(movieId: String): MovieLoadingResult {
        TODO()
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
