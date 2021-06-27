package com.github.mkopylec.furiouscinema.specification

import com.github.mkopylec.furiouscinema.utils.LoadingMovieRequest
import com.github.mkopylec.furiouscinema.utils.LoadingMovieResponse

import static org.springframework.http.HttpStatus.OK

class MoviesLoadingSpec extends BasicSpec {

    def "Should load all movies"() {
        given:
        def loadingMovieRequest1 = new LoadingMovieRequest(movieId: 'tt0232500')
        def loadingMovieResponse1 = new LoadingMovieResponse(imdbID: loadingMovieRequest1.movieId, Title: 'The Fast and the Furious')
        imdb.stubLoadingMovieSuccess(loadingMovieRequest1, loadingMovieResponse1)

        def loadingMovieRequest2 = new LoadingMovieRequest(movieId: 'tt0322259')
        def loadingMovieResponse2 = new LoadingMovieResponse(imdbID: loadingMovieRequest2.movieId, Title: '2 Fast 2 Furious')
        imdb.stubLoadingMovieSuccess(loadingMovieRequest2, loadingMovieResponse2)

        when:
        def response = cinema.loadMovies()

        then:
        with(response) {
            statusCode == OK
            with(body) {
                value.size() == 2
                value.any {
                    it.movieId == loadingMovieRequest1.movieId
                    it.title == loadingMovieResponse1.Title
                }
                value.any {
                    it.movieId == loadingMovieRequest2.movieId
                    it.title == loadingMovieResponse2.Title
                }
                violation == null
            }
        }
    }
}
