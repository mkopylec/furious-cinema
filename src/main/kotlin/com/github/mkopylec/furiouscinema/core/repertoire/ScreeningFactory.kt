package com.github.mkopylec.furiouscinema.core.repertoire

import com.github.mkopylec.furiouscinema.core.NewScreening
import com.github.mkopylec.furiouscinema.core.PriceCurrency
import com.github.mkopylec.furiouscinema.core.movie.Movie

class ScreeningFactory {

    fun newScreening(newScreening: NewScreening, movie: Movie): Screening {
        val runtime = Runtime(newScreening.startTime, movie.runtime.duration)
        val currency = when (newScreening.price.currency) {
            PriceCurrency.USD -> USD
            PriceCurrency.PLN -> PLN
        }
        val price = Price(newScreening.price.amount, currency)
        return Screening(runtime, movie.id, price)
    }
}
