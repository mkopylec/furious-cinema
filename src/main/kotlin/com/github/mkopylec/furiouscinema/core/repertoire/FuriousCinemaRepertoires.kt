package com.github.mkopylec.furiouscinema.core.repertoire

import com.github.mkopylec.furiouscinema.core.NewRepertoire
import com.github.mkopylec.furiouscinema.core.NewScreening
import com.github.mkopylec.furiouscinema.core.movie.Movie
import java.time.DayOfWeek

class FuriousCinemaRepertoires(
    private val repertoires: Repertoires
) {
    private val screeningFactory = ScreeningFactory()

    suspend fun addRepertoire(newRepertoire: NewRepertoire) {
        if (repertoires.forDay(newRepertoire.day) != null) throw RepertoireAlreadyExists
        val repertoire = Repertoire(newRepertoire.day)
        repertoires.save(repertoire)
    }

    suspend fun addScreening(newScreening: NewScreening, movie: Movie) {
        val repertoire = repertoires.forDay(newScreening.day) ?: throw RepertoireNotFound
        val screening = screeningFactory.newScreening(newScreening, movie)
        repertoire.add(screening)
        repertoires.save(repertoire)
    }

    suspend fun loadRepertoire(day: DayOfWeek): Repertoire = repertoires.forDay(day) ?: throw RepertoireNotFound
}
