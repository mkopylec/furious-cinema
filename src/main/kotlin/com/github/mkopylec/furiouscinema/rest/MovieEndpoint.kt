package com.github.mkopylec.furiouscinema.rest

import com.github.mkopylec.furiouscinema.core.FuriousCinema
import com.github.mkopylec.furiouscinema.core.MovieLoadingResult
import com.github.mkopylec.furiouscinema.core.MovieLoadingViolation
import com.github.mkopylec.furiouscinema.core.MoviesLoadingResult
import com.github.mkopylec.furiouscinema.core.NewVote
import com.github.mkopylec.furiouscinema.core.VotingResult
import com.github.mkopylec.furiouscinema.core.VotingViolation
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/movies"], produces = [APPLICATION_JSON_VALUE])
class MovieEndpoint(
    private val cinema: FuriousCinema
) {

    @GetMapping
    suspend fun loadMovies(): ResponseEntity<MoviesLoadingResult> {
        val result = cinema.loadMovies()
        return status(OK).body(result)
    }

    @GetMapping("/{movieId}")
    suspend fun loadMovie(
        @PathVariable movieId: String
    ): ResponseEntity<MovieLoadingResult> {
        val result = cinema.loadMovie(movieId)
        return status(
            when (result.violation) {
                MovieLoadingViolation.MOVIE_NOT_FOUND -> NOT_FOUND
                null -> OK
            }
        ).body(result)
    }

    @PutMapping(path = ["/{movieId}/votes"], consumes = [APPLICATION_JSON_VALUE])
    suspend fun vote(
        @RequestHeader(AUTHORIZATION) authorizationHeader: String,
        @PathVariable movieId: String,
        @RequestBody request: NewVoteRequest
    ): ResponseEntity<VotingResult> {
        val vote = NewVote(authorizationHeader.extractAuthenticationToken(), movieId, request.rating)
        val result = cinema.vote(vote)
        return status(
            when (result.violation) {
                VotingViolation.NOT_AUTHENTICATED -> UNAUTHORIZED
                VotingViolation.NOT_A_MOVIEGOER -> FORBIDDEN
                VotingViolation.MOVIE_NOT_FOUND -> NOT_FOUND
                VotingViolation.ALREADY_VOTED,
                VotingViolation.INVALID_RATING -> UNPROCESSABLE_ENTITY
                null -> OK
            }
        ).body(result)
    }
}
