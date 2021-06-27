package com.github.mkopylec.furiouscinema.infrastructure.persistence

import com.github.mkopylec.furiouscinema.core.repertoire.PLN
import com.github.mkopylec.furiouscinema.core.repertoire.Price
import com.github.mkopylec.furiouscinema.core.repertoire.Repertoire
import com.github.mkopylec.furiouscinema.core.repertoire.Repertoires
import com.github.mkopylec.furiouscinema.core.repertoire.Runtime
import com.github.mkopylec.furiouscinema.core.repertoire.Screening
import com.github.mkopylec.furiouscinema.core.repertoire.USD
import com.github.mkopylec.furiouscinema.infrastructure.resilience4j.ResilienceProvider
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Repository
import java.time.DayOfWeek

@Repository
class MongoDbRepertoires(
    private val mongoDb: ReactiveMongoTemplate,
    private val resilienceProvider: ResilienceProvider
) : Repertoires {

    override suspend fun forDay(day: DayOfWeek): Repertoire? = resilienceProvider.execute("find-repertoire-by-day-in-mongodb") {
        mongoDb.findById(day, RepertoireDocument::class.java).awaitSingleOrNull()
    }?.let {
        try {
            val screenings = it.screenings.map {
                val runtime = Runtime(it.runtime.start, it.runtime.duration)
                val currency = when (it.price.currency) {
                    CurrencyDocument.USD -> USD
                    CurrencyDocument.PLN -> PLN
                }
                val price = Price(it.price.amount, currency)
                Screening(runtime, it.movieId, price)
            }
            Repertoire.fromPersistentState(it.day, screenings)
        } catch (e: Exception) {
            throw IllegalStateException("Error creating ${Repertoire::class} from persistent state", e)
        }
    }

    override suspend fun save(repertoire: Repertoire) {
        val screenings = repertoire.screenings.map {
            val runtime = RuntimeDocument(it.runtime.start, it.runtime.duration)
            val currency = enumValueOf<CurrencyDocument>(it.price.currency.value)
            val price = PriceDocument(it.price.amount, currency)
            ScreeningDocument(runtime, it.movieId, price)
        }
        val document = RepertoireDocument(repertoire.day, screenings)
        resilienceProvider.execute("save-repertoire-to-mongodb") {
            mongoDb.save(document).awaitSingleOrNull()
        }
    }
}
