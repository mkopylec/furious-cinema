package com.github.mkopylec.furiouscinema.core.rating

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.RoundingMode.HALF_UP

// TODO Persisting the rating is not concurrent modification safe. To be so new votes should be stored in separate property to allow repository impl run proper commands on storage.
class Rating private constructor(
    val movieId: String,
    votes: List<Vote>
) {
    var value: BigDecimal = calculateValue(votes)
        private set

    val votes: List<Vote>
        get() = mutableVotes.toList()

    private val mutableVotes: MutableList<Vote> = votes.toMutableList()

    constructor(movieId: String) : this(movieId, listOf())

    fun vote(vote: Vote) {
        if (mutableVotes.any { it.duplicates(vote) }) throw AlreadyVoted
        mutableVotes.add(vote)
        value = calculateValue(mutableVotes)
    }

    private fun calculateValue(votes: List<Vote>): BigDecimal =
        if (votes.isEmpty()) ZERO
        else votes.sumOf { it.rating }.toBigDecimal().setScale(1, HALF_UP) / votes.size.toBigDecimal().setScale(1, HALF_UP)

    companion object {
        fun fromPersistentState(movieId: String, votes: List<Vote>) = Rating(movieId, votes)
    }
}
