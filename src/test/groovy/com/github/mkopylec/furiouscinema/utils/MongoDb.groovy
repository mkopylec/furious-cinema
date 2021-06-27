package com.github.mkopylec.furiouscinema.utils

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Component

@Component
class MongoDb {

    private ReactiveMongoTemplate mongoDb

    protected MongoDb(ReactiveMongoTemplate mongoDb) {
        this.mongoDb = mongoDb
    }

    void clear() {
        mongoDb.collectionNames.collectList().block().each { mongoDb.dropCollection(it).block() }
    }
}
