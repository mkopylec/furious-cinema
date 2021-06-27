package com.github.mkopylec.furiouscinema.core

import java.math.BigDecimal

data class MovieSummary(
    val movieId: String,
    val title: String
)

data class MoviesLoadingResult private constructor(
    val value: List<MovieSummary>?,
    val violation: MoviesLoadingViolation?
) {
    constructor(value: List<MovieSummary>) : this(value, null)
}

enum class MoviesLoadingViolation {
}

data class MovieDetails(
    val movieId: String,
    val title: String,
    val description: String,
    val releaseDate: String,
    val moviegoersRating: BigDecimal,
    val imdbRating: BigDecimal,
    val runtime: String
)

data class MovieLoadingResult private constructor(
    val value: MovieDetails?,
    val violation: MovieLoadingViolation?
) {
    constructor(value: MovieDetails) : this(value, null)
    constructor(violation: MovieLoadingViolation) : this(null, violation)
}

enum class MovieLoadingViolation {
    MOVIE_NOT_FOUND
}

data class NewVote(
    val authenticationToken: String,
    val movieId: String,
    val rating: Int
)

data class VotingResult private constructor(
    val value: Unit?,
    val violation: VotingViolation?
) {
    constructor() : this(null, null)
    constructor(violation: VotingViolation) : this(null, violation)
}

enum class VotingViolation {
    NOT_AUTHENTICATED,
    NOT_A_MOVIEGOER,
    MOVIE_NOT_FOUND,
    ALREADY_VOTED,
    INVALID_RATING
}
