package com.github.mkopylec.furiouscinema.core.repertoire

import java.math.BigDecimal
import java.math.BigDecimal.ZERO

class Price(
    amount: BigDecimal,
    val currency: Currency
) {
    val amount: BigDecimal = if (amount > ZERO) amount else throw InvalidPrice
}

sealed class Currency(
    val value: String
)

object USD : Currency("USD")
object PLN : Currency("PLN")
