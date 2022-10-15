package services

import com.typesafe.scalalogging.StrictLogging
import io.circe
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import io.circe.jawn.decode
import models.Genre

object GenreJsonDecoderService extends StrictLogging {
  implicit val genreDecoder: Decoder[Genre] = deriveDecoder[Genre]
  implicit val genreArrayDecoder: Decoder[GenreArray] = deriveDecoder[GenreArray]

  case class GenreArray(genres: Seq[Genre])

  def decodeJsonToGenresArray(json: String): Either[circe.Error, Seq[Genre]] = {
    val decodeResult = decode[GenreArray](json)
    logger.debug(s"Genre IMDb decode result from json: $json, \n result: ${decodeResult.map(genreArray => genreArray.genres).getOrElse("Error")}")

    decodeResult.map(genreArray => genreArray.genres)
  }
}
