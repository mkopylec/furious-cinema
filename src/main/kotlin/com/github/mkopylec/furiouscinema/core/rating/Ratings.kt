package com.github.mkopylec.furiouscinema.core.rating

interface Ratings {
    suspend fun ofMovie(movieId: String): Rating?
    suspend fun save(rating: Rating)
}
