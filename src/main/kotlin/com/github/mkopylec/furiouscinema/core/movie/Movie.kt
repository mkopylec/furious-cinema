package com.github.mkopylec.furiouscinema.core.movie

import java.math.BigDecimal

class Movie private constructor(
    val id: String,
    val title: String,
    val description: String,
    val releaseDate: String,
    val imdbRating: BigDecimal,
    val runtime: Runtime
) {
    companion object {
        fun fromPersistentState(
            id: String,
            title: String,
            description: String,
            releaseDate: String,
            imdbRating: BigDecimal,
            runtime: Runtime
        ) = Movie(id, title, description, releaseDate, imdbRating, runtime)
    }
}
