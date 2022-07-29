package routes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCode}
import akka.http.scaladsl.server.Directives.{complete, onComplete}
import akka.http.scaladsl.server.Route
import repositories.GenreRepository.{getAllGenres, getGenreByGenreId, updateGenres}
import slick.jdbc.JdbcBackend.Database

import scala.util.{Failure, Success}

object GenreRoute {

  import services.GenreIMDbConverterService._
  import services.GenreIMDbService._

  def getGenreIdFindRoute(genreName: String)(implicit db: Database): Route = {
    onComplete(getGenreByGenreId(genreName)) {
      case Success(genreOption) =>
        genreOption match {
          case Some(genre) => complete(
            StatusCode.int2StatusCode(200),
            HttpEntity(
              ContentTypes.`application/json`,
              s"""{
                 |"GenreName": "${genre.genre}",
                 |"Id": "${genre.id.getOrElse(-1)}"
                 |}""".stripMargin
            )
          )
          case None => complete(
            StatusCode.int2StatusCode(400),
            HttpEntity(
              ContentTypes.`application/json`,
              s"""{
                 |"Message": "Genre with this name haven't been found"
                 |}""".stripMargin
            )
          )
        }

      case Failure(exception) => complete(
        StatusCode.int2StatusCode(500),
        HttpEntity(
          ContentTypes.`application/json`,
          s"""{
             |"Error": "Error handled finding genre",
             | "Exception": "${exception.getMessage}"},
             | "Trace": "${exception.getStackTrace.mkString("Array(", ", ", ")")}"
             | }""".stripMargin
        )
      )
    }
  }

  def getAllGenresRoute(implicit db: Database): Route = {
    onComplete(getAllGenres) {
      case Success(genresSeq) => complete(
        StatusCode.int2StatusCode(200),
        HttpEntity(
          ContentTypes.`application/json`,
          s"""{
             |"Genres": [${genresSeq.map(genre => s""""${genre.genre}"""").mkString(",")}]
             |}""".stripMargin
        )
      )

      case Failure(exception) => complete(
        StatusCode.int2StatusCode(500),
        HttpEntity(
          ContentTypes.`application/json`,
          s"""{
             |"Error": "Error handled during getting all movies",
             | "Exception": "${exception.getMessage}"},
             | "Trace": "${exception.getStackTrace.mkString("Array(", ", ", ")")}"
             | }""".stripMargin
        )
      )
    }
  }

  def getGenresUpdateRoute(genresLanguage: String)(implicit db: Database): Route = {
    onComplete(getGenreIMDbArrayJson(genresLanguage)) {
      case Success(genreIMDbArrayJson) =>
        getGenresUpdateDoneRoute(genreIMDbArrayJson)

      case Failure(exception) => complete(
        StatusCode.int2StatusCode(500),
        HttpEntity(
          ContentTypes.`application/json`,
          s"""{
             |"Error": "Error handled during fetching Genre IMDb Array Json",
             | "Exception": "${exception.getMessage}"},
             | "Trace": "${exception.getStackTrace.mkString("Array(", ", ", ")")}"
             | }""".stripMargin
        )
      )
    }
  }

  def getGenresUpdateDoneRoute(genreIMDbArrayJson: String)(implicit db: Database): Route = {
    decodeJsonToGenreIMDbArray(genreIMDbArrayJson) match {
      case Right(genreIMDbArray) =>
        import implicits.GenreIMDbJsonConverterImplicits._

        val genreArray = genreIMDbArray
          .genres
          .map(convertGenreIMDbEntity2Genre)

        onComplete(updateGenres(genreArray)) {
          case Success(_) => complete(
            StatusCode.int2StatusCode(200),
            HttpEntity(
              ContentTypes.`application/json`,
              s"""{"Message": "Genres were successfully updated"}"""
            )
          )
          case Failure(exception) => complete(
            StatusCode.int2StatusCode(500),
            HttpEntity(
              ContentTypes.`application/json`,
              s"""{
                 |"Error": "Error handled during updating genres",
                 | "Exception": "${exception.getMessage}"},
                 | "Trace": "${exception.getStackTrace.mkString("Array(", ", ", ")")}"
                 | }""".stripMargin
            )
          )
        }
      case Left(exception) =>
        complete(
          StatusCode.int2StatusCode(500),
          HttpEntity(
            ContentTypes.`application/json`,
            s"""{
               |"Error":"Error during conversion of genre array json to genresIMDbArray object.",
               |"Exception": "${exception.getMessage}",
               |"Trace": "${exception.getStackTrace.mkString("Array(", ", ", ")")}"
               |}""".stripMargin
          )
        )
    }
  }
}
