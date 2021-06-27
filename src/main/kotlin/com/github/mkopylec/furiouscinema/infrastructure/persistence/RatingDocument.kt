package com.github.mkopylec.furiouscinema.infrastructure.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("ratings")
data class RatingDocument(
    @Id val movieId: String,
    val votes: List<VoteDocument>
)

data class VoteDocument(
    val userId: Long,
    val rating: Int
)
