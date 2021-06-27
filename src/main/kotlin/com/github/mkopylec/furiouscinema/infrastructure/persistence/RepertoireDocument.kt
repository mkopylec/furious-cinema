package com.github.mkopylec.furiouscinema.infrastructure.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.DayOfWeek

@Document("repertoires")
data class RepertoireDocument(
    @Id val day: DayOfWeek
)
