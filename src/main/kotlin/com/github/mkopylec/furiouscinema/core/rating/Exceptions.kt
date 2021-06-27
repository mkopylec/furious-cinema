package com.github.mkopylec.furiouscinema.core.rating

import com.github.mkopylec.furiouscinema.core.common.FuriousCinemaException

object AlreadyVoted : FuriousCinemaException("ALREADY_VOTED")
object InvalidRating : FuriousCinemaException("INVALID_RATING")
