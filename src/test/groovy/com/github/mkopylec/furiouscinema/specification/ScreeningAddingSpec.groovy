package com.github.mkopylec.furiouscinema.specification

import com.github.mkopylec.furiouscinema.core.NewScreeningPrice
import com.github.mkopylec.furiouscinema.rest.NewRepertoireRequest
import com.github.mkopylec.furiouscinema.rest.NewScreeningRequest
import com.github.mkopylec.furiouscinema.utils.LoadingMovieRequest
import com.github.mkopylec.furiouscinema.utils.LoadingMovieResponse
import groovy.transform.ToString
import spock.lang.Unroll

import static com.github.mkopylec.furiouscinema.core.PriceCurrency.PLN
import static com.github.mkopylec.furiouscinema.core.ScreeningAddingViolation.INVALID_PRICE
import static com.github.mkopylec.furiouscinema.core.ScreeningAddingViolation.NOT_AN_OWNER
import static com.github.mkopylec.furiouscinema.core.ScreeningAddingViolation.NOT_AUTHENTICATED
import static com.github.mkopylec.furiouscinema.core.ScreeningAddingViolation.REPERTOIRE_NOT_FOUND
import static com.github.mkopylec.furiouscinema.core.ScreeningAddingViolation.SCREENINGS_CLASH
import static com.github.mkopylec.furiouscinema.utils.AuthenticationTokens.INVALID_AUTHENTICATION_TOKEN
import static com.github.mkopylec.furiouscinema.utils.AuthenticationTokens.MOVIEGOER_AUTHENTICATION_TOKEN
import static com.github.mkopylec.furiouscinema.utils.AuthenticationTokens.OWNER_AUTHENTICATION_TOKEN
import static java.time.DayOfWeek.SATURDAY
import static java.time.LocalTime.parse
import static org.springframework.http.HttpStatus.FORBIDDEN
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNAUTHORIZED
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

class ScreeningAddingSpec extends BasicSpec {

    def "Should add a screening to a daily repertoire"() {
        given:
        def loadingMovieRequest = new LoadingMovieRequest()
        def loadingMovieResponse = new LoadingMovieResponse(imdbID: loadingMovieRequest.movieId)
        imdb.stubLoadingMovieSuccess(loadingMovieRequest, loadingMovieResponse)

        def newRepertoireRequest = new NewRepertoireRequest(SATURDAY)
        cinema.addRepertoire(OWNER_AUTHENTICATION_TOKEN, newRepertoireRequest)

        def screeningPrice = new NewScreeningPrice(23.99, PLN)
        def request = new NewScreeningRequest(parse('18:00:00'), loadingMovieRequest.movieId, screeningPrice)

        when:
        def response = cinema.addScreening(OWNER_AUTHENTICATION_TOKEN, newRepertoireRequest.day, request)

        then:
        with(response) {
            statusCode == OK
            with(body) {
                value == null
                violation == null
            }
        }
        with(cinema.loadRepertoire(newRepertoireRequest.day).body.value.screenings[0]) {
            startTime == request.startTime
            movieId == loadingMovieRequest.movieId
            title == loadingMovieResponse.Title
            price == "$screeningPrice.amount $screeningPrice.currency"
        }
    }

    def "Should deny to add a screening to a daily repertoire when user is not authenticated"() {
        given:
        def screeningPrice = new NewScreeningPrice(23.99, PLN)
        def request = new NewScreeningRequest(parse('18:00:00'), 'whatever-movie-id', screeningPrice)

        when:
        def response = cinema.addScreening(INVALID_AUTHENTICATION_TOKEN, SATURDAY, request)

        then:
        with(response) {
            statusCode == UNAUTHORIZED
            with(body) {
                value == null
                violation == NOT_AUTHENTICATED
            }
        }
    }

    def "Should deny to add a screening to a daily repertoire when user is an owner"() {
        given:
        def screeningPrice = new NewScreeningPrice(23.99, PLN)
        def request = new NewScreeningRequest(parse('18:00:00'), 'whatever-movie-id', screeningPrice)

        when:
        def response = cinema.addScreening(MOVIEGOER_AUTHENTICATION_TOKEN, SATURDAY, request)

        then:
        with(response) {
            statusCode == FORBIDDEN
            with(body) {
                value == null
                violation == NOT_AN_OWNER
            }
        }
    }

    def "Should not add a screening to a nonexistent daily repertoire"() {
        given:
        def loadingMovieRequest = new LoadingMovieRequest()
        def loadingMovieResponse = new LoadingMovieResponse(imdbID: loadingMovieRequest.movieId)
        imdb.stubLoadingMovieSuccess(loadingMovieRequest, loadingMovieResponse)

        def screeningPrice = new NewScreeningPrice(23.99, PLN)
        def request = new NewScreeningRequest(parse('18:00:00'), loadingMovieRequest.movieId, screeningPrice)

        when:
        def response = cinema.addScreening(OWNER_AUTHENTICATION_TOKEN, SATURDAY, request)

        then:
        with(response) {
            statusCode == NOT_FOUND
            with(body) {
                value == null
                violation == REPERTOIRE_NOT_FOUND
            }
        }
    }

    @Unroll
    def "Should not add a screening #screening1 to a daily repertoire when it clashes with other screening #screening2"() {
        given:
        def loadingMovieRequest1 = new LoadingMovieRequest()
        def loadingMovieResponse1 = new LoadingMovieResponse(imdbID: loadingMovieRequest1.movieId, Runtime: screening1.runtime)
        imdb.stubLoadingMovieSuccess(loadingMovieRequest1, loadingMovieResponse1)

        def loadingMovieRequest2 = new LoadingMovieRequest()
        def loadingMovieResponse2 = new LoadingMovieResponse(imdbID: loadingMovieRequest2.movieId, Runtime: screening2.runtime)
        imdb.stubLoadingMovieSuccess(loadingMovieRequest2, loadingMovieResponse2)

        def newRepertoireRequest = new NewRepertoireRequest(SATURDAY)
        cinema.addRepertoire(OWNER_AUTHENTICATION_TOKEN, newRepertoireRequest)

        def screeningPrice = new NewScreeningPrice(23.99, PLN)
        def request = new NewScreeningRequest(parse(screening1.startTime), loadingMovieRequest1.movieId, screeningPrice)
        cinema.addScreening(OWNER_AUTHENTICATION_TOKEN, newRepertoireRequest.day, request)

        request = new NewScreeningRequest(parse(screening2.startTime), loadingMovieRequest2.movieId, screeningPrice)

        when:
        def response = cinema.addScreening(OWNER_AUTHENTICATION_TOKEN, newRepertoireRequest.day, request)

        then:
        with(response) {
            statusCode == UNPROCESSABLE_ENTITY
            with(body) {
                value == null
                violation == SCREENINGS_CLASH
            }
        }
        with(cinema.loadRepertoire(newRepertoireRequest.day).body.value) {
            screenings.size() == 1
            screenings.any {
                it.movieId == loadingMovieRequest1.movieId
            }
        }

        where:
        screening1                          | screening2
        new Screening('18:00:00', '60 min') | new Screening('17:30:00', '60 min')
        new Screening('18:00:00', '60 min') | new Screening('18:30:00', '60 min')
        new Screening('18:00:00', '60 min') | new Screening('18:10:00', '40 min')
        new Screening('18:00:00', '60 min') | new Screening('17:50:00', '100 min')
    }

    def "Should not add a screening to a daily repertoire when the screening price is invalid"() {
        given:
        def loadingMovieRequest = new LoadingMovieRequest()
        def loadingMovieResponse = new LoadingMovieResponse(imdbID: loadingMovieRequest.movieId)
        imdb.stubLoadingMovieSuccess(loadingMovieRequest, loadingMovieResponse)

        def newRepertoireRequest = new NewRepertoireRequest(SATURDAY)
        cinema.addRepertoire(OWNER_AUTHENTICATION_TOKEN, newRepertoireRequest)

        def screeningPrice = new NewScreeningPrice(-0.99, PLN)
        def request = new NewScreeningRequest(parse('18:00:00'), loadingMovieRequest.movieId, screeningPrice)

        when:
        def response = cinema.addScreening(OWNER_AUTHENTICATION_TOKEN, newRepertoireRequest.day, request)

        then:
        with(response) {
            statusCode == UNPROCESSABLE_ENTITY
            with(body) {
                value == null
                violation == INVALID_PRICE
            }
        }
        with(cinema.loadRepertoire(newRepertoireRequest.day).body.value) {
            screenings.empty
        }
    }

    @ToString(includePackage = false)
    class Screening {

        String startTime
        String runtime

        Screening(String startTime, String runtime) {
            this.startTime = startTime
            this.runtime = runtime
        }
    }
}
