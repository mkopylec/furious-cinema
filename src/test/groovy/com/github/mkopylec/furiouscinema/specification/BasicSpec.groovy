package com.github.mkopylec.furiouscinema.specification

import com.github.mkopylec.furiouscinema.Application
import com.github.mkopylec.furiouscinema.utils.FuriousCinemaHttpClient
import com.github.mkopylec.furiouscinema.utils.ImdbRestStub
import com.github.mkopylec.furiouscinema.utils.MongoDb
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@ActiveProfiles('e2e')
@SpringBootTest(classes = Application, webEnvironment = RANDOM_PORT)
abstract class BasicSpec extends Specification {

    @Autowired
    protected FuriousCinemaHttpClient cinema
    @Shared
    @AutoCleanup
    protected ImdbRestStub imdb = new ImdbRestStub()
    @Autowired
    private MongoDb mongoDb

    void cleanup() {
        imdb.reset()
        mongoDb.clear()
    }
}
