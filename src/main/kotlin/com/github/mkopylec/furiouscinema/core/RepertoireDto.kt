package com.github.mkopylec.furiouscinema.core

import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalTime

data class NewRepertoire(
    val authenticationToken: String,
    val day: DayOfWeek
)

data class RepertoireAddingResult private constructor(
    val value: Unit?,
    val violation: RepertoireAddingViolation?
) {
    constructor() : this(null, null)
    constructor(violation: RepertoireAddingViolation) : this(null, violation)
}

enum class RepertoireAddingViolation {
    NOT_AUTHENTICATED,
    NOT_AN_OWNER,
    REPERTOIRE_ALREADY_EXISTS
}

data class NewScreening(
    val authenticationToken: String,
    val day: DayOfWeek,
    val startTime: LocalTime,
    val movieId: String,
    val price: NewScreeningPrice
)

data class NewScreeningPrice(
    val amount: BigDecimal,
    val currency: PriceCurrency
)

enum class PriceCurrency {
    USD, PLN
}

data class ScreeningAddingResult private constructor(
    val value: Unit?,
    val violation: ScreeningAddingViolation?
) {
    constructor() : this(null, null)
    constructor(violation: ScreeningAddingViolation) : this(null, violation)
}

enum class ScreeningAddingViolation {
    NOT_AUTHENTICATED,
    NOT_AN_OWNER,
    REPERTOIRE_NOT_FOUND,
    SCREENINGS_CLASH,
    INVALID_PRICE
}

data class RepertoireDetails(
    val day: DayOfWeek,
    val screenings: List<ScreeningDetails>
)

data class ScreeningDetails(
    val startTime: LocalTime,
    val movieId: String,
    val title: String,
    val price: String
)

data class RepertoireLoadingResult private constructor(
    val value: RepertoireDetails?,
    val violation: RepertoireLoadingViolation?
) {
    constructor(value: RepertoireDetails) : this(value, null)
    constructor(violation: RepertoireLoadingViolation) : this(null, violation)
}

enum class RepertoireLoadingViolation {
    REPERTOIRE_NOT_FOUND
}
