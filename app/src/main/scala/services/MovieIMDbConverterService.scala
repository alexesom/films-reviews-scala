package services

import com.typesafe.scalalogging.LazyLogging
import io.circe
import io.circe.jawn.decode
import models.{Movie, MovieIMDb}
import repositories.MovieIMDbRepository.{getAgeRatingIdByRating, getCountryIdByCountry, getLanguageIdByLanguage}
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Future

object MovieIMDbConverterService extends LazyLogging {
  import implicits.MovieIMDbConverterImplicits._

  def movieIMDb2MovieConverter(movieIMDb: MovieIMDb, isPublic: Boolean)(implicit db: Database): Future[Movie] = {
    import implicits.AkkaHttpImplicits._

    val ageRatingIdFuture = getAgeRatingIdByRating(movieIMDb.Rated)
    val languageIdFuture = getLanguageIdByLanguage(movieIMDb.Language)
    val countryIdFuture = getCountryIdByCountry(movieIMDb.Country)
    val runtime = getRuntimeInIntFromStringResponse(movieIMDb.Runtime)

    ageRatingIdFuture.flatMap(ageRatingId =>
      languageIdFuture.flatMap(languageId =>
        countryIdFuture.map(countryId =>
          Movie(
            title = movieIMDb.Title,
            release = Option(movieIMDb.Released),
            ageRatingId = Option(ageRatingId),
            actors = Option(movieIMDb.Actors),
            plot = Option(movieIMDb.Plot),
            runtime = Option(runtime),
            poster = Option(movieIMDb.Poster),
            languageId = Option(languageId),
            countryId = Option(countryId),
            awards = Option(movieIMDb.Awards),
            imdbId = Option(movieIMDb.imdbID),
            isPublic = Option(isPublic)
          )
        )
      )
    )
  }

  def getRuntimeInIntFromStringResponse(runtimeString: String): Int = {
    "^\\D*(\\d+)".r findFirstMatchIn runtimeString match {
      case Some(value) => value.group(1).toInt
      case None => 0
    }
  }

  def decodeJsonToMovieIMDb(json: String): Either[circe.Error, MovieIMDb] = {
    val decodeResult = decode[MovieIMDb](json)
    logger.debug(s"Movie IMDb decode result from json: $json, \n result: ${decodeResult.getOrElse("Error")}")

    decodeResult
  }
}
