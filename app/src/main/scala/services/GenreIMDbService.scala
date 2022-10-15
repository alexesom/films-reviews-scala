package services

import akka.http.scaladsl.model.Uri
import com.typesafe.scalalogging.StrictLogging
import services.MovieService.sendRequest

import scala.concurrent.Future

object GenreIMDbService extends StrictLogging {
  def getGenresJson(genresLanguage: String): Future[String] = {
    import implicits.Configuration._

    logger.debug(s"Starting building of TMDb Uri")
    val queryBuilder = Uri.Query.newBuilder

    queryBuilder += ("language" -> genresLanguage)
    queryBuilder += ("api_key" -> apiKeyTMDb)

    val requestUri: Uri = Uri(uriTMDb + genresUpdateUriPart).withQuery(queryBuilder.result())
    logger.debug(s"End building of TMDb Uri: $requestUri")
    val requestData = sendRequest(requestUri)

    requestData
  }
}
