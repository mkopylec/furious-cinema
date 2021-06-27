package com.github.mkopylec.furiouscinema.core.repertoire

import java.time.DayOfWeek

interface Repertoires {
    suspend fun forDay(day: DayOfWeek): Repertoire?
    suspend fun save(repertoire: Repertoire)
}
