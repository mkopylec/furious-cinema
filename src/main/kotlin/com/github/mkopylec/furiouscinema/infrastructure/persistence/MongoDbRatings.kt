package com.github.mkopylec.furiouscinema.infrastructure.persistence

import com.github.mkopylec.furiouscinema.core.rating.Rating
import com.github.mkopylec.furiouscinema.core.rating.Ratings
import com.github.mkopylec.furiouscinema.core.rating.Vote
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Repository

@Repository
class MongoDbRatings(
    private val mongoDb: ReactiveMongoTemplate
) : Ratings {

    override suspend fun ofMovie(movieId: String): Rating? = mongoDb.findById(movieId, RatingDocument::class.java)
        .awaitSingleOrNull()
        ?.let {
            try {
                Rating.fromPersistentState(it.movieId, it.votes.map { Vote(it.userId, it.rating) })
            } catch (e: Exception) {
                throw IllegalStateException("Error creating ${Rating::class} from persistent state", e)
            }
        }

    override suspend fun save(rating: Rating) {
        val document = RatingDocument(rating.movieId, rating.votes.map { VoteDocument(it.userId, it.rating) })
        mongoDb.save(document).awaitSingleOrNull()
    }
}
