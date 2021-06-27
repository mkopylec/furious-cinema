package com.github.mkopylec.furiouscinema.core.repertoire

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.RoundingMode.HALF_UP

class Price(
    amount: BigDecimal,
    val currency: Currency
) {
    val amount: BigDecimal = if (amount > ZERO) amount.setScale(2, HALF_UP) else throw InvalidPrice

    override fun toString(): String = "$amount ${currency.value}"
}

sealed class Currency(
    val value: String
)

object USD : Currency("USD")
object PLN : Currency("PLN")
