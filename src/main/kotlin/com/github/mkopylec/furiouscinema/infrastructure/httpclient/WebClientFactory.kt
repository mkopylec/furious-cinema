package com.github.mkopylec.furiouscinema.infrastructure.httpclient

import io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClient.builder
import reactor.netty.http.client.HttpClient

@Component
class WebClientFactory {

    fun createWebClient(properties: HttpClientProperties): WebClient {
        val client = HttpClient.create()
            .option(CONNECT_TIMEOUT_MILLIS, properties.timeout.connect.toSeconds().toInt())
            .doOnConnected {
                it.addHandler(ReadTimeoutHandler(properties.timeout.read.toSeconds().toInt()))
                it.addHandler(WriteTimeoutHandler(properties.timeout.write.toSeconds().toInt()))
            }
        return builder()
            .clientConnector(ReactorClientHttpConnector(client))
            .baseUrl(properties.baseUrl.toString())
            .build()
    }
}
