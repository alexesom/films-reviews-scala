package models

import slick.jdbc.PostgresProfile.api._

import java.time.LocalDate

case class Movie(
                  id: Option[Long] = Some(0),
                  title: String,
                  release: Option[LocalDate],
                  ageRatingId: Option[Long],
                  actors: Option[String],
                  plot: Option[String],
                  runtime: Option[Int],
                  poster: Option[String],
                  languageId: Option[Long],
                  countryId: Option[Long],
                  awards: Option[String],
                  imdbId: Option[String],
                  isPublic: Option[Boolean]
                ) {
  def areAllAttributesDefined: Boolean = {
    release.isDefined &&
      ageRatingId.isDefined &&
      actors.isDefined &&
      plot.isDefined &&
      runtime.isDefined &&
      poster.isDefined &&
      languageId.isDefined &&
      countryId.isDefined &&
      awards.isDefined &&
      imdbId.isDefined &&
      isPublic.isDefined
  }
}

case class MovieResponse()

class MoviesTable(tag: Tag) extends Table[Movie](tag, "movies") {
  //implicit val idColumnType: BaseColumnType[Age] = MappedColumnType.base[Age, Int](_.value, Age(_))

  def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

  def title = column[String]("title", O.Unique)

  def release = column[Option[LocalDate]]("release")

  def ageRatingId = column[Option[Long]]("age_rating_id")

  def actors = column[Option[String]]("actors")

  def plot = column[Option[String]]("plot")

  def runtime = column[Option[Int]]("runtime")

  def poster = column[Option[String]]("poster")

  def languageId = column[Option[Long]]("language_id")

  def countryId = column[Option[Long]]("country_id")

  def awards = column[Option[String]]("awards")

  def imdbId = column[Option[String]]("imdb_id", O.Unique)

  def isPublic = column[Option[Boolean]]("is_public")

  def * = (
    id,
    title,
    release,
    ageRatingId,
    actors,
    plot,
    runtime,
    poster,
    languageId,
    countryId,
    awards,
    imdbId,
    isPublic
  ) <> (Movie.tupled, Movie.unapply)
}

object MoviesTable {
  lazy val moviesTableQuery = TableQuery[MoviesTable]
}