package com.github.mkopylec.furiouscinema.core.rating

class Vote(
    val userId: Long,
    rating: Int
) {
    val rating: Int = if (rating in 1..5) rating else throw InvalidRating

    fun duplicates(vote: Vote): Boolean = vote.userId == userId
}
