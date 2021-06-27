package com.github.mkopylec.furiouscinema.core

import com.github.mkopylec.furiouscinema.core.authentication.Authentications
import com.github.mkopylec.furiouscinema.core.authentication.FuriousCinemaAuthentications
import com.github.mkopylec.furiouscinema.core.common.FuriousCinemaException
import com.github.mkopylec.furiouscinema.core.movie.FuriousCinemaMovies
import com.github.mkopylec.furiouscinema.core.movie.Movies
import com.github.mkopylec.furiouscinema.core.rating.FuriousCinemaRatings
import com.github.mkopylec.furiouscinema.core.rating.Ratings
import com.github.mkopylec.furiouscinema.core.repertoire.FuriousCinemaRepertoires
import com.github.mkopylec.furiouscinema.core.repertoire.Repertoires
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.stereotype.Service
import java.time.DayOfWeek

@Service
class FuriousCinema(
    properties: FuriousCinemaProperties,
    authentications: Authentications,
    movies: Movies,
    ratings: Ratings,
    repertoires: Repertoires
) {
    private val logger: Logger = getLogger(FuriousCinema::class.java)
    private val authentications = FuriousCinemaAuthentications(authentications)
    private val movies = FuriousCinemaMovies(properties.movie, movies)
    private val ratings = FuriousCinemaRatings(ratings)
    private val repertoires = FuriousCinemaRepertoires(repertoires)

    suspend fun loadMovies(): MoviesLoadingResult {
        val movies = movies.loadMovies()
        return MoviesLoadingResult(movies.map { MovieSummary(it.id, it.title) })
    }

    suspend fun loadMovie(movieId: String): MovieLoadingResult = try {
        val movie = movies.loadMovie(movieId)
        val rating = ratings.loadRating(movie)
        MovieLoadingResult(
            MovieDetails(
                movieId = movie.id,
                title = movie.title,
                description = movie.description,
                releaseDate = movie.releaseDate,
                moviegoersRating = rating.value,
                imdbRating = movie.imdbRating,
                runtime = movie.runtime.value
            )
        )
    } catch (e: FuriousCinemaException) {
        logger.warn(e.message, e)
        MovieLoadingResult(enumValueOf<MovieLoadingViolation>(e.violation))
    }

    suspend fun vote(vote: NewVote): VotingResult = try {
        val authentication = authentications.authenticateMoviegoer(vote.authenticationToken)
        val movie = movies.loadMovie(vote.movieId)
        ratings.vote(vote, movie, authentication)
        VotingResult()
    } catch (e: FuriousCinemaException) {
        logger.warn(e.message, e)
        VotingResult(enumValueOf(e.violation))
    }

    suspend fun addRepertoire(repertoire: NewRepertoire): RepertoireAddingResult = try {
        authentications.authenticateOwner(repertoire.authenticationToken)
        repertoires.addRepertoire(repertoire)
        RepertoireAddingResult()
    } catch (e: FuriousCinemaException) {
        logger.warn(e.message, e)
        RepertoireAddingResult(enumValueOf(e.violation))
    }

    suspend fun addScreening(screening: NewScreening): ScreeningAddingResult {
        TODO()
    }

    suspend fun loadRepertoire(day: DayOfWeek): RepertoireLoadingResult {
        TODO()
    }
}
