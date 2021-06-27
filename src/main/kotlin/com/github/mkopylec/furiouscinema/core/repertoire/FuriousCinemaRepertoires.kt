package com.github.mkopylec.furiouscinema.core.repertoire

import com.github.mkopylec.furiouscinema.core.NewRepertoire

class FuriousCinemaRepertoires(
    private val repertoires: Repertoires
) {
    suspend fun addRepertoire(newRepertoire: NewRepertoire) {
        if (repertoires.forDay(newRepertoire.day) != null) throw RepertoireAlreadyExists
        val repertoire = Repertoire(newRepertoire.day)
        repertoires.save(repertoire)
    }
}
