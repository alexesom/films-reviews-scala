package models

import slick.jdbc.PostgresProfile.api._

case class DirectorMovie(id: Long, director_id: Long, movie_id: Long)

class DirectorsMoviesTable(tag: Tag) extends Table[DirectorMovie](tag, "directors_movies") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def director_id = column[Long]("director_id")

  def movie_id = column[Long]("movie_id")

  def director_id_fk = foreignKey(
    "director_id_fk",
    column[Long]("director_id"),
    TableQuery[DirectorsTable]
  )(_.id)

  def movie_id_fk = foreignKey(
    "movie_id_fk",
    column[Option[Long]]("movie_id"),
    TableQuery[MoviesTable]
  )(_.id)

  def * = (id, director_id, movie_id) <> (DirectorMovie.tupled, DirectorMovie.unapply)

  lazy val directorsMoviesTableQuery = TableQuery[DirectorsMoviesTable]
}

object DirectorsMoviesTable {
  lazy val directorsMoviesTableQuery = TableQuery[DirectorsMoviesTable]
}