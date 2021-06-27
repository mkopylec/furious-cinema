package com.github.mkopylec.furiouscinema.infrastructure.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalTime

@Document("repertoires")
data class RepertoireDocument(
    @Id val day: DayOfWeek,
    val screenings: List<ScreeningDocument>
)

data class ScreeningDocument(
    val runtime: RuntimeDocument,
    val movieId: String,
    val price: PriceDocument
)

data class RuntimeDocument(
    val start: LocalTime,
    val duration: Duration
)

data class PriceDocument(
    val amount: BigDecimal,
    val currency: CurrencyDocument
)

enum class CurrencyDocument {
    USD, PLN
}
