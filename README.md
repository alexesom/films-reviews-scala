# Movies reviews API

![](https://docs.scala-lang.org/resources/img/scala-logo.png)

The main purpuse of this project was to create rotten tomatoes or kinopoisk like API
to allow users to share their reviews and to recommend movies to another users.

## API:
```
POST /movies/ {*movie json*}
POST /uploads/genres 
POST or DELETE /movies/imdb/tt0111161
GET /genres/all
GET /genres/id/*genre_name*
POST /comments/ {*comments json*}
GET /movies?page=1&pageSize=25&publicMovies=true
GET /comments?page=1&pageSize=25
```

## Used technologies:
  - Scala 2.13
  - Slick 3.3
  - Akka Http 10.2
  - Akka/Akka Typed 2.6
  - Spray-JSON 1.3 && Akka Spray-Json 2.6
  - SLF4J with Logback 1.2
  - Typesafe config 1.4
  - Akka-quartz-scheduler 2.6
  - Guava 23.0
  - Circe 0.14
  - Flyway-sbt 7.4
  - Docker
  - Postgres 
  
  **Architecture**: Monolith
