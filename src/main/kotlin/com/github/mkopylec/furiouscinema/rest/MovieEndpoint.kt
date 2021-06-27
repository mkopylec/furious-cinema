package com.github.mkopylec.furiouscinema.rest

import com.github.mkopylec.furiouscinema.core.MovieLoadingResult
import com.github.mkopylec.furiouscinema.core.MoviesLoadingResult
import com.github.mkopylec.furiouscinema.core.VotingResult
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/movies"], produces = [APPLICATION_JSON_VALUE])
class MovieEndpoint {

    @GetMapping
    suspend fun loadMovies(): ResponseEntity<MoviesLoadingResult> {
        TODO()
    }

    @GetMapping("/{movieId}")
    suspend fun loadMovie(
        @PathVariable movieId: String,
    ): ResponseEntity<MovieLoadingResult> {
        TODO()
    }

    @PutMapping(path = ["/{movieId}/votes"], consumes = [APPLICATION_JSON_VALUE])
    suspend fun vote(
        @RequestHeader(AUTHORIZATION) authorizationHeader: String,
        @PathVariable movieId: String,
        @RequestBody request: NewVoteRequest
    ): ResponseEntity<VotingResult> {
        TODO()
    }
}
