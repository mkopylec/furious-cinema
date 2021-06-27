package com.github.mkopylec.furiouscinema.infrastructure.mongodb

import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration
import java.util.concurrent.TimeUnit.MILLISECONDS

@Configuration
class MongoDbConfiguration(
    private val properties: MongoDbProperties
) {
    @Bean
    fun mongoDbSettings(): MongoClientSettingsBuilderCustomizer {
        return MongoClientSettingsBuilderCustomizer {
            it.applyToSocketSettings {
                it.connectTimeout(properties.timeout.connect.toMillis().toInt(), MILLISECONDS)
                it.readTimeout(properties.timeout.read.toMillis().toInt(), MILLISECONDS)
            }
        }
    }
}

@ConstructorBinding
@ConfigurationProperties("furious-cinema.infrastructure.mongodb")
class MongoDbProperties(
    @NestedConfigurationProperty
    val timeout: TimeoutProperties
)

@ConstructorBinding
class TimeoutProperties(
    val connect: Duration,
    val read: Duration
)
