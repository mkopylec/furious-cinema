package com.github.mkopylec.furiouscinema.core.movie

import java.time.Duration
import java.time.Duration.ofMinutes

class Runtime(
    val value: String
) {
    val duration: Duration = value.toPositiveDuration()

    private fun String.toPositiveDuration(): Duration {
        val parts = split(" ")
        if (parts[1] != "min") throw InvalidRuntime
        val minutes = parts[0].toLong()
        if (minutes < 1) throw InvalidRuntime
        return ofMinutes(minutes)
    }
}
