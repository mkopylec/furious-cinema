# Furious Cinema

A backend service for a small cinema, which only plays movies from the [Fast & Furious](https://en.wikipedia.org/wiki/The_Fast_and_the_Furious) franchise.

## Running
Before running, make sure:
- you have JDK 11 or higher installed
- you have no [MongoDB](https://www.mongodb.com/) instance running locally

To run the application execute:
```shell
./gradlew bootRun --args='--furious-cinema.infrastructure.http-client.omdb.api-key=<your omdb api key>'
```
To run tests execute:
```shell
./gradlew test
```

## About
Here are some details about the project.

### Technology stack
Tools used to implement the project:
- [Spring Boot WebFlux](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
- [Kotlin coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Resilience4j](https://resilience4j.readme.io/docs/getting-started)
- [Embedded MongoDB](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo)
- [OpenAPI 3](https://springdoc.org/)
- [Spock](https://spockframework.org/)
- [WireMock](http://wiremock.org/)

### Architecture
The project contains a single module.
The codebase is organized more or less according to [my article](https://blog.allegro.tech/2019/12/grouping-and-organizing-classes.html).
The module implements [hexagonal architecture](https://alistair.cockburn.us/hexagonal-architecture/).
Application logic resides in the [`core`](/src/main/kotlin/com/github/mkopylec/furiouscinema/core) package which is bounded by inbound and outbound ports.
The only inbound port is [`FuriousCinema`](/src/main/kotlin/com/github/mkopylec/furiouscinema/core/FuriousCinema.kt).
This is the API of the application logic.
Inbound adapters lie inside the [`rest`](/src/main/kotlin/com/github/mkopylec/furiouscinema/rest) package.
REST endpoints [`MovieEndpoint`](/src/main/kotlin/com/github/mkopylec/furiouscinema/rest/MovieEndpoint.kt) and [`RepertoireEndpoint`](/src/main/kotlin/com/github/mkopylec/furiouscinema/rest/RepertoireEndpoint.kt) plays their roles.
The outbound ports are
[`Authentications`](/src/main/kotlin/com/github/mkopylec/furiouscinema/core/authentication/Authentications.kt), 
[`Movies`](/src/main/kotlin/com/github/mkopylec/furiouscinema/core/movie/Movies.kt),
[`Ratings`](/src/main/kotlin/com/github/mkopylec/furiouscinema/core/rating/Ratings.kt), 
[`Repertoires`](/src/main/kotlin/com/github/mkopylec/furiouscinema/core/repertoire/Repertoires.kt).
The corresponding outbound adapters, which are concrete implementations of the outbound ports, lie inside the [`infrastructure`](/src/main/kotlin/com/github/mkopylec/furiouscinema/infrastructure) package.
Inside the `core` package, source code is organized using [`Tactical DDD`](https://thedomaindrivendesign.io/what-is-tactical-design/) building blocks.
The package is divided into small mini-modules, one per [`Aggregate`](https://martinfowler.com/bliki/DDD_Aggregate.html).
Every mini-module encapsulates a part of business logic behind the API through its facade.

### Tests
There only integration (E2E) tests in the project.
That type of tests assures the most that the project will work expected in real world, although they are relatively slow.
For bigger project testing application logic in separation should be considered.

### Naming
Consistent naming is one of the things that makes code more readable.
The project sticks to the one name per operation and per entity throughout the whole codebase (endpoints, application logic, tests).

### Open API
To view and test the REST API of the project run the project and go to [Swagger UI](http://localhost:8080/swagger-ui.html).
To get the raw JSON description of the REST API go to [API documentation](http://localhost:8080/v3/api-docs).

### TODO
Here is the list of what should be done besides of what already exists:
- some generic logging mechanism (based on function or [AspectJ](https://www.eclipse.org/aspectj/)) to log inbound and outbound ports operations.
- [ArchUnit](https://www.archunit.org/) tests
