package models

import slick.jdbc.PostgresProfile.api._

case class GenreMovie(id: Option[Long], genre_id: Long, movie_id: Long)

class GenresMoviesTable(tag: Tag) extends Table[GenreMovie](tag, "genres_movies") {
  def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

  def genre_id = column[Long]("genre_id")

  def movie_id = column[Long]("movie_id")

  def genre_id_fk = foreignKey(
    "genre_id_fk",
    column[Option[Long]]("genre_id"),
    TableQuery[GenresTable]
  )(_.id)

  def movie_id_fk = foreignKey(
    "movie_id_fk",
    column[Option[Long]]("movie_id"),
    TableQuery[MoviesTable]
  )(_.id)

  def * = (id, genre_id, movie_id) <> (GenreMovie.tupled, GenreMovie.unapply)

  lazy val genresMoviesTableQuery = TableQuery[GenresMoviesTable]
}

object GenresMoviesTable {
  lazy val genresMoviesTableQuery = TableQuery[GenresMoviesTable]
}