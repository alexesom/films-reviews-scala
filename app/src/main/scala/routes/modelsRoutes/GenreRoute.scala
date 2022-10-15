package routes.modelsRoutes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCode}
import akka.http.scaladsl.server.Directives.{complete, onComplete}
import akka.http.scaladsl.server.Route
import models.Genre
import repositories.GenreRepository.{getAllGenres, getGenreByGenreId, updateGenres}
import slick.jdbc.JdbcBackend.Database

import scala.util.{Failure, Success}

object GenreRoute {

  import services.GenreJsonDecoderService._
  import services.GenreIMDbService._

  def findGenreByGenreIdRoute(genreName: String)(implicit db: Database): Route = {
    onComplete(getGenreByGenreId(genreName)) {
      case Success(genreOption) =>
        completeWithGenreOrError(genreOption)

      case Failure(exception) =>
        genresUpdateFailedRoute(exception)
    }
  }

  def updateGenresRoute(genresLanguage: String)(implicit db: Database): Route = {
    onComplete(getGenresJson(genresLanguage)) {
      case Success(genresJson) =>
        genresUpdateDoneRoute(genresJson)

      case Failure(exception) =>
        genresUpdateFailedRoute(exception)
    }
  }

  def getAllGenresRoute(implicit db: Database): Route = {
    onComplete(getAllGenres) {
      case Success(genresSeq) =>
        completeWithAllGenres(genresSeq)

      case Failure(exception) =>
        genresUpdateFailedRoute(exception)
    }
  }

  private def completeWithAllGenres(genresSeq: Seq[Genre]) = {
    complete(
      StatusCode.int2StatusCode(200),
      HttpEntity(
        ContentTypes.`application/json`,
        s"""{
           |"genres": [${genresSeq.map(genre => s""""${genre.name}"""").mkString(",")}]
           |}""".stripMargin
      )
    )
  }

  private def completeWithGenreOrError(genreOption: Option[Genre]): Route = {
    genreOption match {
      case Some(genre) => complete(
        StatusCode.int2StatusCode(200),
        HttpEntity(
          ContentTypes.`application/json`,
          s"""{
             |"genre_name": "${genre.name}",
             |"id": "${genre.id}"
             |}""".stripMargin
        )
      )
      case None => complete(
        StatusCode.int2StatusCode(404),
        HttpEntity(
          ContentTypes.`application/json`,
          s"""{
             |"message": "Genre with this name haven't been found"
             |}""".stripMargin
        )
      )
    }
  }

  private def genresUpdateFailedRoute(exception: Throwable) = {
    complete(
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

  private def genresUpdateDoneRoute(genresJson: String)(implicit db: Database): Route = {
    decodeJsonToGenresArray(genresJson) match {
      case Right(fetchedGenres) =>
        completeWithUpdateSuccessOrError(fetchedGenres)
      case Left(exception) =>
        complete(
          StatusCode.int2StatusCode(500),
          HttpEntity(
            ContentTypes.`application/json`,
            s"""{
               |"error":"Error during conversion of genre array json to genresIMDbArray object.",
               |"exception": "${exception.getMessage}",
               |"trace": "${exception.getStackTrace.mkString("Array(", ", ", ")")}"
               |}""".stripMargin
          )
        )
    }
  }

  private def completeWithUpdateSuccessOrError(fetchedGenres: Seq[Genre])(implicit db: Database): Route = {
    onComplete(updateGenres(fetchedGenres)) {
      case Success(_) => complete(
        StatusCode.int2StatusCode(200),
        HttpEntity(
          ContentTypes.`application/json`,
          s"""{"message": "Genres were successfully updated"}"""
        )
      )
      case Failure(exception) =>
        genresUpdateFailedRoute(exception)
    }
  }
}
