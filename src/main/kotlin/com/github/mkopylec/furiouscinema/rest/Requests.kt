package com.github.mkopylec.furiouscinema.rest

import com.github.mkopylec.furiouscinema.core.NewScreeningPrice
import java.time.DayOfWeek
import java.time.LocalTime

data class NewRepertoireRequest(
    val day: DayOfWeek
)

data class NewScreeningRequest(
    val startTime: LocalTime,
    val movieId: String,
    val price: NewScreeningPrice
)

data class NewVoteRequest(
    val rating: Int
)

fun String.extractAuthenticationToken(): String = replace("bearer", "", ignoreCase = true).trim()
