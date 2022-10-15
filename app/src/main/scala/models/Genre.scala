package models

import slick.jdbc.PostgresProfile.api._

case class Genre(
                  id: Long = 0,
                  name: String
                )

class GenresTable(tag: Tag) extends Table[Genre](tag, "genres") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name", O.Unique)

  def * = (id, name) <> (Genre.tupled, Genre.unapply)
}

object GenresTable {
  lazy val genresTableQuery = TableQuery[GenresTable]
}