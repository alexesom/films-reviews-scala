package implicits

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder, HCursor, Json}
import models.{MovieIMDb, Ratings}
import services.ConverterService.parseJsonStringToLocalDate

import java.time.LocalDate

object MovieIMDbConverterImplicits {

  implicit val encodeMovieIMDbLocalDate: Encoder[LocalDate] = (a: LocalDate) => {
    Json.fromString(a.toString)
  }
  implicit val decodeMovieIMDbLocalDate: Decoder[LocalDate] = (c: HCursor) => {
    c.value.as[String]
      .map(stringDate => parseJsonStringToLocalDate(stringDate))
  }

  implicit val ratingsDecoder: Decoder[Ratings] = deriveDecoder[Ratings]
  implicit val movieIMDbDecoder: Decoder[MovieIMDb] = deriveDecoder[MovieIMDb]
  implicit val ratingsEncoder: Encoder[Ratings] = deriveEncoder[Ratings]
  implicit val movieIMDbEncoder: Encoder[MovieIMDb] = deriveEncoder[MovieIMDb]
}
