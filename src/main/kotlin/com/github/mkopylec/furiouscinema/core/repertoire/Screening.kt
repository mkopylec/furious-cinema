package com.github.mkopylec.furiouscinema.core.repertoire

import java.time.Duration
import java.time.LocalTime
import java.time.LocalTime.MAX

class Screening(
    val runtime: Runtime,
    val movieId: String,
    val price: Price
) {
    fun clashesWith(screening: Screening): Boolean = runtime.clashesWith(screening.runtime)
}

class Runtime(
    val start: LocalTime,
    val duration: Duration
) {
    private val end: LocalTime =
        if (MAX.toSecondOfDay() - start.toSecondOfDay() > duration.toSeconds()) start + duration
        else throw InvalidRuntime

    fun clashesWith(runtime: Runtime): Boolean = !(start >= runtime.end || end <= runtime.start)
}
