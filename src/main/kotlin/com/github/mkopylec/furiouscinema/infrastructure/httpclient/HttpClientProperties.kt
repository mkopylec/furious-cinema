package com.github.mkopylec.furiouscinema.infrastructure.httpclient

import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.NestedConfigurationProperty
import java.net.URI
import java.time.Duration

abstract class HttpClientProperties(
    val baseUrl: URI,
    @NestedConfigurationProperty
    val timeout: TimeoutProperties
)

@ConstructorBinding
class TimeoutProperties(
    val connect: Duration,
    val read: Duration,
    val write: Duration
)
