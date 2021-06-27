package com.github.mkopylec.furiouscinema.infrastructure.persistence

import com.github.mkopylec.furiouscinema.core.repertoire.Repertoire
import com.github.mkopylec.furiouscinema.core.repertoire.Repertoires
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Repository
import java.time.DayOfWeek

@Repository
class MongoDbRepertoires(
    private val mongoDb: ReactiveMongoTemplate
) : Repertoires {

    override suspend fun forDay(day: DayOfWeek): Repertoire? = mongoDb.findById(day, RepertoireDocument::class.java)
        .awaitSingleOrNull()
        ?.let {
            try {
                Repertoire.fromPersistentState(it.day)
            } catch (e: Exception) {
                throw IllegalStateException("Error creating ${Repertoire::class} from persistent state", e)
            }
        }

    override suspend fun save(repertoire: Repertoire) {
        val document = RepertoireDocument(repertoire.day)
        mongoDb.save(document).awaitSingleOrNull()
    }
}
