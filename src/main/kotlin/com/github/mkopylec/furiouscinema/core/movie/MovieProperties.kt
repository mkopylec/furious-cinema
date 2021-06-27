package com.github.mkopylec.furiouscinema.core.movie

import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
class MovieProperties(
    val availableMovieIds: List<String>
)
