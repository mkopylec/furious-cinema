package com.github.mkopylec.furiouscinema.core

import com.github.mkopylec.furiouscinema.core.movie.MovieProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.NestedConfigurationProperty

@ConstructorBinding
@ConfigurationProperties("furious-cinema.core")
class FuriousCinemaProperties(
    @NestedConfigurationProperty
    val movie: MovieProperties
)
