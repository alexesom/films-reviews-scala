package services

import com.typesafe.scalalogging.StrictLogging
import io.circe
import io.circe.jawn.decode
import models.GenreIMDbArray

object GenreIMDbConverterService extends StrictLogging {
  import implicits.GenreIMDbJsonConverterImplicits._

  def decodeJsonToGenreIMDbArray(json: String): Either[circe.Error, GenreIMDbArray] = {
    val decodeResult = decode[GenreIMDbArray](json)
    logger.debug(s"Genre IMDb decode result from json: $json, \n result: ${decodeResult.getOrElse("Error")}")

    decodeResult
  }
}
