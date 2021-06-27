package com.github.mkopylec.furiouscinema.utils

import org.springframework.http.HttpStatus

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.get
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import static org.springframework.http.HttpHeaders.CONTENT_TYPE
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

class ImdbRestStub extends RestStub {

    ImdbRestStub() {
        super(10000)
    }

    void stubLoadingMovieSuccess(LoadingMovieRequest request = new LoadingMovieRequest(), LoadingMovieResponse response = new LoadingMovieResponse()) {
        stubLoadingMovie(request.apiKey, request.movieId, OK, response)
    }

    private void stubLoadingMovie(String apiKey, String movieId, HttpStatus status, Object responseBody) {
        server.stubFor(get(urlEqualTo("/?apikey=$apiKey&i=$movieId"))
                .willReturn(aResponse()
                        .withStatus(status.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(toJson(responseBody))))
    }
}
