package com.github.mkopylec.furiouscinema.specification

import com.github.mkopylec.furiouscinema.utils.LoadingMovieRequest
import com.github.mkopylec.furiouscinema.utils.LoadingMovieResponse

import static com.github.mkopylec.furiouscinema.core.MovieLoadingViolation.MOVIE_NOT_FOUND
import static java.math.BigDecimal.ZERO
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

class MovieLoadingSpec extends BasicSpec {

    def "Should load a single movie"() {
        given:
        def loadingMovieRequest = new LoadingMovieRequest()
        def loadingMovieResponse = new LoadingMovieResponse(imdbID: loadingMovieRequest.movieId)
        imdb.stubLoadingMovieSuccess(loadingMovieRequest, loadingMovieResponse)

        when:
        def response = cinema.loadMovie(loadingMovieRequest.movieId)

        then:
        with(response) {
            statusCode == OK
            with(body) {
                with(value) {
                    movieId == loadingMovieRequest.movieId
                    title == loadingMovieResponse.Title
                    description == loadingMovieResponse.Plot
                    releaseDate == loadingMovieResponse.Released
                    moviegoersRating == ZERO
                    imdbRating == loadingMovieResponse.imdbRating
                    runtime == loadingMovieResponse.Runtime
                }
                violation == null
            }
        }
    }

    def "Should not load a single movie when it doesn't exist"() {
        when:
        def response = cinema.loadMovie('nonexistent-movie-id')

        then:
        with(response) {
            statusCode == NOT_FOUND
            with(body) {
                value == null
                violation == MOVIE_NOT_FOUND
            }
        }
    }
}
