package com.github.mkopylec.furiouscinema.infrastructure.persistence

import com.github.mkopylec.furiouscinema.core.rating.Rating
import com.github.mkopylec.furiouscinema.core.rating.Ratings
import com.github.mkopylec.furiouscinema.core.rating.Vote
import com.github.mkopylec.furiouscinema.infrastructure.resilience4j.ResilienceProvider
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Repository

@Repository
class MongoDbRatings(
    private val mongoDb: ReactiveMongoTemplate,
    private val resilienceProvider: ResilienceProvider
) : Ratings {

    override suspend fun ofMovie(movieId: String): Rating? = resilienceProvider.execute("find-rating-by-movie-id-in-mongodb") {
        mongoDb.findById(movieId, RatingDocument::class.java).awaitSingleOrNull()
    }?.let {
        try {
            Rating.fromPersistentState(it.movieId, it.votes.map { Vote(it.userId, it.rating) })
        } catch (e: Exception) {
            throw IllegalStateException("Error creating ${Rating::class} from persistent state", e)
        }
    }

    override suspend fun save(rating: Rating) {
        val document = RatingDocument(rating.movieId, rating.votes.map { VoteDocument(it.userId, it.rating) })
        resilienceProvider.execute("save-rating-to-mongodb") {
            mongoDb.save(document).awaitSingleOrNull()
        }
    }
}
