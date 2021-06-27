package com.github.mkopylec.furiouscinema.core.authentication

import com.github.mkopylec.furiouscinema.core.common.FuriousCinemaException

object NotAuthenticated : FuriousCinemaException("NOT_AUTHENTICATED")
object NotAMoviegoer : FuriousCinemaException("NOT_A_MOVIEGOER")
