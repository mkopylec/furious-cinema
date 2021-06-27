package com.github.mkopylec.furiouscinema.specification

import com.github.mkopylec.furiouscinema.rest.NewVoteRequest
import com.github.mkopylec.furiouscinema.utils.LoadingMovieRequest
import com.github.mkopylec.furiouscinema.utils.LoadingMovieResponse
import spock.lang.Unroll

import static com.github.mkopylec.furiouscinema.core.VotingViolation.ALREADY_VOTED
import static com.github.mkopylec.furiouscinema.core.VotingViolation.INVALID_RATING
import static com.github.mkopylec.furiouscinema.core.VotingViolation.MOVIE_NOT_FOUND
import static com.github.mkopylec.furiouscinema.core.VotingViolation.NOT_AUTHENTICATED
import static com.github.mkopylec.furiouscinema.core.VotingViolation.NOT_A_MOVIEGOER
import static com.github.mkopylec.furiouscinema.utils.AuthenticationTokens.INVALID_AUTHENTICATION_TOKEN
import static com.github.mkopylec.furiouscinema.utils.AuthenticationTokens.MOVIEGOER_AUTHENTICATION_TOKEN
import static com.github.mkopylec.furiouscinema.utils.AuthenticationTokens.OTHER_MOVIEGOER_AUTHENTICATION_TOKEN
import static com.github.mkopylec.furiouscinema.utils.AuthenticationTokens.OWNER_AUTHENTICATION_TOKEN
import static java.math.BigDecimal.ZERO
import static org.springframework.http.HttpStatus.FORBIDDEN
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNAUTHORIZED
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

class VotingLoadingSpec extends BasicSpec {

    def "Should join moviegoers votes into one rating"() {
        given:
        def loadingMovieRequest = new LoadingMovieRequest()
        def loadingMovieResponse = new LoadingMovieResponse(imdbID: loadingMovieRequest.movieId)
        imdb.stubLoadingMovieSuccess(loadingMovieRequest, loadingMovieResponse)

        def request = new NewVoteRequest(5)
        cinema.vote(MOVIEGOER_AUTHENTICATION_TOKEN, loadingMovieRequest.movieId, request)
        request = new NewVoteRequest(2)

        when:
        def response = cinema.vote(OTHER_MOVIEGOER_AUTHENTICATION_TOKEN, loadingMovieRequest.movieId, request)

        then:
        with(response) {
            statusCode == OK
            with(body) {
                value == null
                violation == null
            }
        }
        with(cinema.loadMovie(loadingMovieRequest.movieId).body.value) {
            moviegoersRating == 3.5
        }
    }

    def "Should deny voting for a movie when user is not authenticated"() {
        given:
        def request = new NewVoteRequest(5)

        when:
        def response = cinema.vote(INVALID_AUTHENTICATION_TOKEN, 'whatever-movie-id', request)

        then:
        with(response) {
            statusCode == UNAUTHORIZED
            with(body) {
                value == null
                violation == NOT_AUTHENTICATED
            }
        }
    }

    def "Should deny voting for a movie when user is not a moviegoer"() {
        given:
        def request = new NewVoteRequest(5)

        when:
        def response = cinema.vote(OWNER_AUTHENTICATION_TOKEN, 'whatever-movie-id', request)

        then:
        with(response) {
            statusCode == FORBIDDEN
            with(body) {
                value == null
                violation == NOT_A_MOVIEGOER
            }
        }
    }

    def "Should not vote for a nonexistent movie"() {
        given:
        def request = new NewVoteRequest(5)

        when:
        def response = cinema.vote(MOVIEGOER_AUTHENTICATION_TOKEN, 'nonexistent-movie-id', request)

        then:
        with(response) {
            statusCode == NOT_FOUND
            with(body) {
                value == null
                violation == MOVIE_NOT_FOUND
            }
        }
    }

    def "Should deny voting for a movie more than once"() {
        given:
        def loadingMovieRequest = new LoadingMovieRequest()
        def loadingMovieResponse = new LoadingMovieResponse(imdbID: loadingMovieRequest.movieId)
        imdb.stubLoadingMovieSuccess(loadingMovieRequest, loadingMovieResponse)

        def request = new NewVoteRequest(5)
        cinema.vote(MOVIEGOER_AUTHENTICATION_TOKEN, loadingMovieRequest.movieId, request)
        request = new NewVoteRequest(2)

        when:
        def response = cinema.vote(MOVIEGOER_AUTHENTICATION_TOKEN, loadingMovieRequest.movieId, request)

        then:
        with(response) {
            statusCode == UNPROCESSABLE_ENTITY
            with(body) {
                value == null
                violation == ALREADY_VOTED
            }
        }
        with(cinema.loadMovie(loadingMovieRequest.movieId).body.value) {
            moviegoersRating == 5.0
        }
    }

    @Unroll
    def "Should not vote for a movie when rating is invalid: #rating"() {
        given:
        def loadingMovieRequest = new LoadingMovieRequest()
        def loadingMovieResponse = new LoadingMovieResponse(imdbID: loadingMovieRequest.movieId)
        imdb.stubLoadingMovieSuccess(loadingMovieRequest, loadingMovieResponse)

        def request = new NewVoteRequest(rating)

        when:
        def response = cinema.vote(MOVIEGOER_AUTHENTICATION_TOKEN, loadingMovieRequest.movieId, request)

        then:
        with(response) {
            statusCode == UNPROCESSABLE_ENTITY
            with(body) {
                value == null
                violation == INVALID_RATING
            }
        }
        with(cinema.loadMovie(loadingMovieRequest.movieId).body.value) {
            moviegoersRating == ZERO
        }

        where:
        rating << [0, 6]
    }
}
