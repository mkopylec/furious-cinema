package com.github.mkopylec.furiouscinema.core

import java.math.BigDecimal

data class MovieSummary(
    val movieId: String,
    val title: String
)

data class MoviesLoadingResult private constructor(
    override val value: List<MovieSummary>?,
    override val violation: MoviesLoadingViolation?
) : Result<MoviesLoadingViolation, List<MovieSummary>>(value, violation) {

    constructor(value: List<MovieSummary>) : this(value, null)
}

enum class MoviesLoadingViolation : Violation {
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
    override val value: MovieDetails?,
    override val violation: MovieLoadingViolation?
) : Result<MovieLoadingViolation, MovieDetails>(value, violation) {

    constructor(value: MovieDetails) : this(value, null)
    constructor(violation: MovieLoadingViolation) : this(null, violation)
}

enum class MovieLoadingViolation : Violation {
    MOVIE_NOT_FOUND
}

data class VotingResult private constructor(
    override val value: Unit?,
    override val violation: VotingViolation?
) : Result<VotingViolation, Unit>(value, violation) {

    constructor() : this(null, null)
    constructor(violation: VotingViolation) : this(null, violation)
}

enum class VotingViolation : Violation {
    NOT_AUTHENTICATED,
    NOT_A_MOVIEGOER,
    MOVIE_NOT_FOUND,
    ALREADY_VOTED,
    INVALID_RATING
}
