package com.github.mkopylec.furiouscinema.core.repertoire

import java.time.DayOfWeek

// TODO Persisting the repertoire is not concurrent modification safe. To be so new screenings should be stored in separate property to allow repository impl run proper commands on storage.
class Repertoire private constructor(
    val day: DayOfWeek,
    screenings: List<Screening>
) {
    val screenings: List<Screening>
        get() = mutableScreenings.toList()

    private val mutableScreenings: MutableList<Screening> = screenings.toMutableList()

    constructor(day: DayOfWeek) : this(day, listOf())

    fun add(screening: Screening) {
        if (mutableScreenings.any { it.clashesWith(screening) }) throw ScreeningsClash
        mutableScreenings.add(screening)
    }

    companion object {
        fun fromPersistentState(date: DayOfWeek, screenings: List<Screening>) = Repertoire(date, screenings)
    }
}
