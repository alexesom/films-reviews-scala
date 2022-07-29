package services

import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.Get
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, Uri}
import com.typesafe.scalalogging.StrictLogging
import models.Movie

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

object MovieService extends StrictLogging {

  import implicits.AkkaHttpImplicits._

  def getMovieListJsonByTitleAndRelease(movie: Movie, rowUriOMDb: String, apiKeyOMDb: String): Future[String] = {
    logger.debug(s"Starting building of OMDb Uri for movie title: ${movie.title}")
    val queryBuilder = Uri.Query.newBuilder

    queryBuilder += ("s" -> movie.title)
    queryBuilder += ("y" -> movie.release.map(_.getYear).getOrElse("").toString)
    queryBuilder += ("type" -> "movie")
    queryBuilder += ("apikey" -> apiKeyOMDb)

    val requestUri: Uri = Uri(rowUriOMDb).withQuery(queryBuilder.result())
    logger.debug(s"End building of OMDb Uri for movie title: ${movie.title}, uri: $requestUri")
    val requestData = sendRequest(requestUri)

    requestData
  }

  def sendRequest(requestUri: Uri): Future[String] = {
    logger.debug(s"Starting sending http request to OMDb for request uri: $requestUri")
    val responseFuture: Future[HttpResponse] = Http().singleRequest(Get(requestUri))
    val entityFuture: Future[HttpEntity.Strict] = responseFuture.flatMap(response => response.entity.toStrict(2.seconds))

    entityFuture.map(entity => entity.data.utf8String)
  }

  def fetchAllMoviesFromTMDb = {
    //TODO
  }
}
