package com.github.mkopylec.furiouscinema.utils

import com.github.mkopylec.furiouscinema.core.MovieLoadingResult
import com.github.mkopylec.furiouscinema.core.MoviesLoadingResult
import com.github.mkopylec.furiouscinema.core.RepertoireAddingResult
import com.github.mkopylec.furiouscinema.core.RepertoireLoadingResult
import com.github.mkopylec.furiouscinema.core.ScreeningAddingResult
import com.github.mkopylec.furiouscinema.core.VotingResult
import com.github.mkopylec.furiouscinema.rest.NewRepertoireRequest
import com.github.mkopylec.furiouscinema.rest.NewScreeningRequest
import com.github.mkopylec.furiouscinema.rest.NewVoteRequest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

import java.time.DayOfWeek

import static org.springframework.http.HttpHeaders.EMPTY
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpMethod.PUT

@Component
class FuriousCinemaHttpClient {

    private TestRestTemplate httpClient

    protected FuriousCinemaHttpClient(TestRestTemplate httpClient) {
        this.httpClient = httpClient
    }

    ResponseEntity<MoviesLoadingResult> loadMovies() {
        sendRequest('/movies', GET, null, new ParameterizedTypeReference<MoviesLoadingResult>() {})
    }

    ResponseEntity<MovieLoadingResult> loadMovie(String movieId) {
        sendRequest("/movies/$movieId", GET, null, new ParameterizedTypeReference<MovieLoadingResult>() {})
    }

    ResponseEntity<VotingResult> vote(String authenticationToken, String movieId, NewVoteRequest request) {
        sendRequest("/movies/$movieId/votes", PUT, request, new ParameterizedTypeReference<VotingResult>() {}, createAuthorizedHeaders(authenticationToken))
    }

    ResponseEntity<RepertoireAddingResult> addRepertoire(String authenticationToken, NewRepertoireRequest request) {
        sendRequest('/repertoires', POST, request, new ParameterizedTypeReference<RepertoireAddingResult>() {}, createAuthorizedHeaders(authenticationToken))
    }

    ResponseEntity<ScreeningAddingResult> addScreening(String authenticationToken, DayOfWeek day, NewScreeningRequest request) {
        sendRequest("/repertoires/$day/screenings", PUT, request, new ParameterizedTypeReference<ScreeningAddingResult>() {}, createAuthorizedHeaders(authenticationToken))
    }

    ResponseEntity<RepertoireLoadingResult> loadRepertoire(DayOfWeek day) {
        sendRequest("/repertoires/$day", GET, null, new ParameterizedTypeReference<RepertoireLoadingResult>() {})
    }

    private <B> ResponseEntity<B> sendRequest(String url, HttpMethod method, Object requestBody, ParameterizedTypeReference<B> responseBodyType, HttpHeaders requestHeaders = EMPTY) {
        def request = RequestEntity.method(method, url)
                .headers(requestHeaders)
                .body(requestBody)
        httpClient.exchange(request, responseBodyType)
    }

    private static HttpHeaders createAuthorizedHeaders(String authenticationToken) {
        new HttpHeaders().tap { setBearerAuth(authenticationToken) }
    }
}
