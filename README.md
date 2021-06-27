# Furious Cinema

A backend service for a small cinema, which only plays movies from the [Fast & Furious](https://en.wikipedia.org/wiki/The_Fast_and_the_Furious) franchise.

## Running

Before running, make sure:

- you have JDK 11 or higher installed
- you have no [MongoDB](https://www.mongodb.com/) instance running locally

To run the application execute:

```shell
./gradlew bootRun --args='--furious-cinema.infrastructure.http-client.imdb.api-key=<your imdb api key>'
```

To run tests execute:

```shell
./gradlew test
```
