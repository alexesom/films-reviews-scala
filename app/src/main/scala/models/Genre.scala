package models

import slick.jdbc.PostgresProfile.api._

case class Genre(
                  id: Option[Long] = Some(0),
                  genre: String
                )

class GenresTable(tag: Tag) extends Table[Genre](tag, "genres") {
  def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

  def genre = column[String]("genre", O.Unique)

  def * = (id, genre) <> (Genre.tupled, Genre.unapply)
}

object GenresTable {
  lazy val genresTableQuery = TableQuery[GenresTable]
}