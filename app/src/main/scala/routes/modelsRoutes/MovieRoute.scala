package routes.modelsRoutes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCode}
import akka.http.scaladsl.server.Directives.{as, complete, onComplete}
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.StrictLogging
import io.circe.syntax.EncoderOps
import models.Movie
import models.json.MovieJsonProtocol
import repositories.MovieRepository
import repositories.MovieRepository.{getMoviesPage, insertMovie}
import services.GeneralService.optionalEntity
import slick.jdbc.JdbcBackend.Database
import spray.json.enrichAny

import scala.util.{Failure, Success}

object MovieRoute extends MovieJsonProtocol with StrictLogging {

  import implicits.Configuration._
  import services.MovieService._

  def getInsertMovieRoute(implicit db: Database): Route = {
    optionalEntity(as[Movie]) {
      case Some(movie) =>
        if (movie.areAllAttributesDefined) {
          getMovieFulfilledInsertRoute(movie)
        } else {
          complete(
            StatusCode.int2StatusCode(400),
            HttpEntity(ContentTypes.`application/json`,
              """{"Error":"Not all fields defined."}"""))
        }
      case _ =>
        complete(
          StatusCode.int2StatusCode(400),
          HttpEntity(
            ContentTypes.`application/json`,
            """{"Error":"Incorrect JSON provided."}""")

        )
    }
  }

  def getMovieFulfilledInsertRoute(movie: Movie)(implicit db: Database): Route = {
    //TODO movie.validate
    logger.debug(s"Trying to insert fulfilled movie into database: $movie")

    onComplete(insertMovie(movie)) {
      case Success(_) =>
        complete(
          HttpEntity(
            ContentTypes.`application/json`,
            movie.toJson.toString)
        )

      case Failure(exception) =>
        logger.debug(s"Movie insertion failed: ${movie.toString}, ${exception.getStackTrace.mkString("Array(", ", ", ")")}")

        complete(StatusCode.int2StatusCode(400),
          HttpEntity(ContentTypes.`application/json`,
            s"""{
               |"Error":"Movie insertion failed.",
               |"Exception": "${exception.getMessage.replace("\"", "\'")}",
               |"Trace": "${exception.getStackTrace.mkString("Array(", ", ", ")")}"
               |}""".stripMargin
          )
        )
    }
  }

  def getMovieListByTitleAndReleaseRoute(movie: Movie): Route = {
    onComplete(getMovieListJsonByTitleAndRelease(movie, uriOMDb, apiKeyOMDb)) {
      case Success(value) => println(value)
        complete(
          HttpEntity(
            ContentTypes.`application/json`,
            value //TODO
          )
        )

      case Failure(_) =>
        complete(
          StatusCode.int2StatusCode(400),
          HttpEntity(
            ContentTypes.`application/json`,
            """{"Error":"Incorrect JSON provided."}""") //why

        )
    }
  }

  def getDeleteByIMDbIdRoute(imdbId: String)(implicit db: Database): Route = {
    val deleteMovieFutureOption = MovieRepository.deleteMovieByIMDbId(imdbId)

    onComplete(deleteMovieFutureOption) {
      case Success(movieOption) =>
        movieOption match {
          case Some(movie) =>
            complete(
              HttpEntity(
                ContentTypes.`application/json`,
                movie.asJson.toString()
              )
            )
          case None =>
            complete(
              StatusCode.int2StatusCode(400),
              HttpEntity(
                ContentTypes.`application/json`,
                s"""{"Error": "Movie not found"}"""
              )
            )
        }

      case Failure(exception) =>
        complete(
          StatusCode.int2StatusCode(400),
          HttpEntity(
            ContentTypes.`application/json`,
            s"""{
               |"Error": "Unable to delete movie by movie IMDb id.",
               |"Exception": "${exception.getMessage.replace("\"", "\'")}",
               |"Trace": "${exception.getStackTrace.mkString("Array(", ", ", ")")}"
               |}""".stripMargin
          )
        )
    }
  }

  def getMoviesPageRoute(page: Int, pageSize: Int, publicMovies: Boolean)(implicit db: Database): Route = {
    onComplete(getMoviesPage(page, pageSize, publicMovies)) {
      case Success(moviesSeq) =>
        complete(
          StatusCode.int2StatusCode(200),
          HttpEntity(
            ContentTypes.`application/json`,
            moviesSeq.asJson.toString()
          )
        )
      case Failure(exception) =>
        complete(
          StatusCode.int2StatusCode(400),
          HttpEntity(
            ContentTypes.`application/json`,
            s"""{
               |"Error": "Error occurred while getting movies by page: $page, pageSize: $pageSize, publicMovies: $publicMovies",
               |"Exception": "${exception.getMessage.replace("\"", "\'")}",
               |"Trace": "${exception.getStackTrace.mkString("Array(", ", ", ")")}"
               |}""".stripMargin
          )
        )
    }
  }
}
