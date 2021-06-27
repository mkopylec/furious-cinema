package com.github.mkopylec.furiouscinema.core.rating

import com.github.mkopylec.furiouscinema.core.NewVote
import com.github.mkopylec.furiouscinema.core.authentication.Authentication
import com.github.mkopylec.furiouscinema.core.movie.Movie

class FuriousCinemaRatings(
    private val ratings: Ratings
) {
    suspend fun loadRating(movie: Movie): Rating = ratings.ofMovie(movie.id) ?: Rating(movie.id)

    suspend fun vote(newVote: NewVote, movie: Movie, authentication: Authentication) {
        val rating = loadRating(movie)
        val vote = Vote(authentication.userId, newVote.rating)
        rating.vote(vote)
        ratings.save(rating)
    }
}
