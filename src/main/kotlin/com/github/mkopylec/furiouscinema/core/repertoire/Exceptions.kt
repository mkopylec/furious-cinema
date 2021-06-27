package com.github.mkopylec.furiouscinema.core.repertoire

import com.github.mkopylec.furiouscinema.core.common.FuriousCinemaException

object RepertoireAlreadyExists : FuriousCinemaException("REPERTOIRE_ALREADY_EXISTS")
object RepertoireNotFound : FuriousCinemaException("REPERTOIRE_NOT_FOUND")
object ScreeningsClash : FuriousCinemaException("SCREENINGS_CLASH")
object InvalidPrice : FuriousCinemaException("INVALID_PRICE")
object InvalidRuntime : FuriousCinemaException("INVALID_RUNTIME")
