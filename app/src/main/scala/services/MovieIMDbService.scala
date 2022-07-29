package services

import akka.http.scaladsl.model.Uri
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.Future

object MovieIMDbService extends StrictLogging {
  import MovieService.sendRequest

  def getMovieIMDbJsonByIMDbId(movieIMDbId: String): Future[String] = {
    import implicits.Configuration._

    logger.debug(s"Starting building of OMDb Uri for IMDbId: $movieIMDbId")
    val queryBuilder = Uri.Query.newBuilder

    queryBuilder += ("i" -> movieIMDbId)
    queryBuilder += ("type" -> "movie")
    queryBuilder += ("apikey" -> apiKeyOMDb)

    val requestUri: Uri = Uri(uriOMDb).withQuery(queryBuilder.result())
    logger.debug(s"End building of OMDb Uri for IMDbId: $movieIMDbId, uri: $requestUri")
    val requestData = sendRequest(requestUri)

    requestData
  }
}
