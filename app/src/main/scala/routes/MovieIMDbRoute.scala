package routes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCode}
import akka.http.scaladsl.server.Directives.{complete, onComplete, onSuccess}
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.StrictLogging
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax.EncoderOps
import models.Movie
import repositories.MovieRepository.insertMovie
import services.MovieIMDbService.getMovieIMDbJsonByIMDbId
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Future
import scala.util.{Failure, Success}

object MovieIMDbRoute extends StrictLogging {
  def getInsertMovieByIdRoute(movieIMDbId: String, isPublic: Boolean)(implicit db: Database): Route = {
    val movieIMDbJsonFuture = getMovieIMDbJsonByIMDbId(movieIMDbId)

    onComplete(movieIMDbJsonFuture) {
      case Success(movieIMDbJson) =>
        getMovieIMDbInsertDoneRoute(movieIMDbJson, isPublic)

      case Failure(exception) =>
        complete(
          StatusCode.int2StatusCode(400),
          HttpEntity(
            ContentTypes.`application/json`,
            s"""{
               |"Error":"Movie by id insertion failed."
               |"Exception": "${exception.getMessage.replace("\"", "\'")}",
               |"Trace": "${exception.getStackTrace.mkString("Array(", ", ", ")")}"
               |}""".stripMargin
          )
        )
    }
  }

  def getMovieIMDbInsertDoneRoute(movieIMDbJson: String, isPublic: Boolean)(implicit db: Database): Route = {

    import services.MovieIMDbConverterService._

    val movieFuture: Future[Movie] =
      decodeJsonToMovieIMDb(movieIMDbJson) match {
        case Right(movieResponse) =>
          movieIMDb2MovieConverter(movieResponse, isPublic)

        case Left(circleException) => Future.failed(circleException)
      }

    onSuccess(movieFuture) { movie =>
      implicit val movieEncoder: Encoder[Movie] = deriveEncoder[Movie]
      val insertMovieIndex = insertMovie(movie)

      logger.info(s"Inserting movie by IMDb ID completed successfully, movie: ${movie.asJson.toString}," +
        s" index in db:$insertMovieIndex")
      complete(
        HttpEntity(
          ContentTypes.`application/json`,
          movieIMDbJson
        )
      )
    }
  }
}
