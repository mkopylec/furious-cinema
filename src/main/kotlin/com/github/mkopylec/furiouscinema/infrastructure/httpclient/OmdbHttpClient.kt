package com.github.mkopylec.furiouscinema.infrastructure.httpclient

import com.github.mkopylec.furiouscinema.infrastructure.resilience4j.ResilienceProvider
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.math.BigDecimal
import java.net.URI

@Component
class OmdbHttpClient(
    private val properties: OmdbHttpClientProperties,
    private val resilienceProvider: ResilienceProvider,
    webClientFactory: WebClientFactory
) {
    private val client: WebClient = webClientFactory.createWebClient(properties)

    suspend fun loadMovie(id: String): OmdbMovieResponse = resilienceProvider.execute("load-movie-by-id-from-omdb") {
        client
            .get()
            .uri { it.queryParam("apikey", properties.apiKey).queryParam("i", id).build() }
            .retrieve()
            .awaitBody()
    }
}

data class OmdbMovieResponse(
    val imdbID: String,
    val Title: String,
    val Released: String,
    val Plot: String,
    val imdbRating: BigDecimal,
    val Runtime: String
)

@ConstructorBinding
@ConfigurationProperties("furious-cinema.infrastructure.http-client.omdb")
class OmdbHttpClientProperties(
    baseUrl: URI,
    timeout: TimeoutProperties,
    val apiKey: String
) : HttpClientProperties(baseUrl, timeout)
