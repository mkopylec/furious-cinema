package com.github.mkopylec.furiouscinema.core.movie

import com.github.mkopylec.furiouscinema.core.common.FuriousCinemaException

object MovieNotFound : FuriousCinemaException("MOVIE_NOT_FOUND")
object InvalidRuntime : FuriousCinemaException("INVALID_RUNTIME")
