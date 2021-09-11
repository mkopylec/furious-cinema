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
All the application logic in encapsulated behind its API.
Inbound adapters lie inside the [`rest`](/src/main/kotlin/com/github/mkopylec/furiouscinema/rest) package.
REST endpoints [`MovieEndpoint`](/src/main/kotlin/com/github/mkopylec/furiouscinema/rest/MovieEndpoint.kt) and [`RepertoireEndpoint`](/src/main/kotlin/com/github/mkopylec/furiouscinema/rest/RepertoireEndpoint.kt) play their roles.
The outbound ports are
[`Authentications`](/src/main/kotlin/com/github/mkopylec/furiouscinema/core/authentication/Authentications.kt), 
[`Movies`](/src/main/kotlin/com/github/mkopylec/furiouscinema/core/movie/Movies.kt),
[`Ratings`](/src/main/kotlin/com/github/mkopylec/furiouscinema/core/rating/Ratings.kt), 
[`Repertoires`](/src/main/kotlin/com/github/mkopylec/furiouscinema/core/repertoire/Repertoires.kt).
The corresponding outbound adapters, which are concrete implementations of the outbound ports, lie inside the [`infrastructure`](/src/main/kotlin/com/github/mkopylec/furiouscinema/infrastructure) package.
Inside the `core` package, source code is organized using [`Tactical DDD`](https://thedomaindrivendesign.io/what-is-tactical-design/) building blocks.
The `core` package is divided into small mini-modules, one per [aggregate](https://martinfowler.com/bliki/DDD_Aggregate.html).
Every mini-module encapsulates a part of the business logic behind its facade's API.

### Tests
There are only integration (E2E) tests in the project.
That type of tests assures the most that the project will work as expected in the real world, although they are relatively slow.
For bigger projects testing application logic in separation should be considered.

### Naming
Consistent naming is one of the things that makes code more readable.
The project sticks to the one name per operation and per entity throughout the whole codebase (endpoints, application logic, tests).

### Open API
To view and test the REST API of the project run the project and go to [Swagger UI](http://localhost:8080/swagger-ui.html).
To get the raw JSON description of the REST API go to [API documentation](http://localhost:8080/v3/api-docs).

### TODO
Here is the list of what should be done besides of what already exists:
- some generic logging mechanism (based on function or [AspectJ](https://www.eclipse.org/aspectj/)) to automatically log inbound and outbound ports operations.
- [ArchUnit](https://www.archunit.org/) tests

### Use cases invariants
#### Load movies
Loads all available movies.
Invariants:
 - movie must have ID
 - movie must have title

#### Load a movie
Loads a single movie.
Invariants:
- movie must exist
- movie must have ID
- movie must have title
- movie must have description
- movie must have release date
- movie must have moviegoers rating
- movie must have imdb rating
- movie must have runtime

#### Vote for a movie
Recalculates the overall moviegoers rating of the movie because of the new user vote.
Invariants:
 - user must be a registered moviegoer
 - movie must exist
 - user mustn't vote multiple times for the same movie
 - vote has rating that can be a number 1...5
 - overall movie rating is an average of all user votes

#### Add a repertoire
Cinema owner adds an empty repertoire for a single day.
Invariants:
 - user must be a cinema owner
 - only one repertoire per day can be added

#### Add a repertoire screening
Cinema owner adds a single movie screening to a daily repertoire by specifying a start time and price.
Invariants:
 - user must be a cinema owner
 - movie must exist
 - repertoire for a requested day must exist
 - repertoire must have a free time slot for the whole screening runtime
 - no more than one screening can be showed at the time
 - whole screening must run on the same day
 - price can not be 0 or lower

#### Load a repertoire
Loads all daily repertoire screenings.
Invariants:
 - repertoire must exist
 - screening must have start time
 - screening must have movie title
 - screening must have price
