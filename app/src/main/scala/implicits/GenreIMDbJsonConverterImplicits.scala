package implicits

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import models.{Genre, GenreIMDbArray, GenreIMDbEntity}

import scala.language.implicitConversions

object GenreIMDbJsonConverterImplicits {
  implicit val genreIMDbEncoder: Encoder[GenreIMDbArray] = deriveEncoder[GenreIMDbArray]
  implicit val genreIMDbEntityEncoder: Encoder[GenreIMDbEntity] = deriveEncoder[GenreIMDbEntity]
  implicit val genreIMDbDecoder: Decoder[GenreIMDbArray] = deriveDecoder[GenreIMDbArray]
  implicit val genreIMDbEntityDecoder: Decoder[GenreIMDbEntity] = deriveDecoder[GenreIMDbEntity]

  implicit def convertGenreIMDbEntity2Genre(genreIMDbEntity: GenreIMDbEntity): Genre = {
    Genre(genre = genreIMDbEntity.name)
  }
}
