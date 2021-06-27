package com.github.mkopylec.furiouscinema.core.movie

interface Movies {
    suspend fun ofId(id: String): Movie
    suspend fun ofIds(ids: List<String>): List<Movie>
}
