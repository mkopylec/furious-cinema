package com.github.mkopylec.furiouscinema.specification

import com.github.mkopylec.furiouscinema.core.NewScreeningPrice
import com.github.mkopylec.furiouscinema.rest.NewRepertoireRequest
import com.github.mkopylec.furiouscinema.rest.NewScreeningRequest
import com.github.mkopylec.furiouscinema.utils.LoadingMovieRequest
import com.github.mkopylec.furiouscinema.utils.LoadingMovieResponse

import static com.github.mkopylec.furiouscinema.core.PriceCurrency.PLN
import static com.github.mkopylec.furiouscinema.core.RepertoireLoadingViolation.REPERTOIRE_NOT_FOUND
import static com.github.mkopylec.furiouscinema.utils.AuthenticationTokens.OWNER_AUTHENTICATION_TOKEN
import static java.time.DayOfWeek.SATURDAY
import static java.time.LocalTime.parse
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

class RepertoireLoadingSpec extends BasicSpec {

    def "Should load a daily repertoire"() {
        given:
        def loadingMovieRequest = new LoadingMovieRequest()
        def loadingMovieResponse = new LoadingMovieResponse(imdbID: loadingMovieRequest.movieId)
        imdb.stubLoadingMovieSuccess(loadingMovieRequest, loadingMovieResponse)

        def newRepertoireRequest = new NewRepertoireRequest(SATURDAY)
        cinema.addRepertoire(OWNER_AUTHENTICATION_TOKEN, newRepertoireRequest)

        def screeningPrice = new NewScreeningPrice(23.99, PLN)
        def screeningRequest = new NewScreeningRequest(parse('18:00:00'), loadingMovieRequest.movieId, screeningPrice)
        cinema.addScreening(OWNER_AUTHENTICATION_TOKEN, newRepertoireRequest.day, screeningRequest)

        when:
        def response = cinema.loadRepertoire(newRepertoireRequest.day)

        then:
        with(response) {
            statusCode == OK
            with(body) {
                with(value) {
                    day == newRepertoireRequest.day
                    screenings.size() == 1
                    screenings.any {
                        it.startTime == screeningRequest.startTime
                        it.movieId == loadingMovieRequest.movieId
                        it.title == loadingMovieResponse.Title
                        it.price == "$screeningPrice.amount $screeningPrice.currency"
                    }
                }
                violation == null
            }
        }
    }

    def "Should not load a daily repertoire when it doesn't exist"() {
        when:
        def response = cinema.loadRepertoire(SATURDAY)

        then:
        with(response) {
            statusCode == NOT_FOUND
            with(body) {
                value == null
                violation == REPERTOIRE_NOT_FOUND
            }
        }
    }
}
