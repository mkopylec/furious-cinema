package com.github.mkopylec.furiouscinema.core

import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalTime

data class RepertoireAddingResult private constructor(
    override val value: Unit?,
    override val violation: RepertoireAddingViolation?
) : Result<RepertoireAddingViolation, Unit>(value, violation) {

    constructor() : this(null, null)
    constructor(violation: RepertoireAddingViolation) : this(null, violation)
}

enum class RepertoireAddingViolation : Violation {
    NOT_AUTHENTICATED,
    NOT_AN_OWNER,
    REPERTOIRE_ALREADY_EXISTS
}

data class NewScreeningPrice(
    val amount: BigDecimal,
    val currency: PriceCurrency
)

enum class PriceCurrency {
    USD, PLN
}

data class ScreeningAddingResult private constructor(
    override val value: Unit?,
    override val violation: ScreeningAddingViolation?
) : Result<ScreeningAddingViolation, Unit>(value, violation) {

    constructor() : this(null, null)
    constructor(violation: ScreeningAddingViolation) : this(null, violation)
}

enum class ScreeningAddingViolation : Violation {
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
    override val value: RepertoireDetails?,
    override val violation: RepertoireLoadingViolation?
) : Result<RepertoireLoadingViolation, RepertoireDetails>(value, violation) {

    constructor(value: RepertoireDetails) : this(value, null)
    constructor(violation: RepertoireLoadingViolation) : this(null, violation)
}

enum class RepertoireLoadingViolation : Violation {
    REPERTOIRE_NOT_FOUND
}
