package com.github.mkopylec.furiouscinema.core.repertoire

import java.time.DayOfWeek

class Repertoire(
    val day: DayOfWeek
) {
    companion object {
        fun fromPersistentState(day: DayOfWeek) = Repertoire(day)
    }
}
