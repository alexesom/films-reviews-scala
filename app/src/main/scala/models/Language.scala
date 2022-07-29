package models

import slick.jdbc.PostgresProfile.api._

case class Language(id: Long = 0L, lang: String)

class LanguagesTable(tag: Tag) extends Table[Language](tag, "languages") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def language = column[String]("lang")

  def * = (id, language) <> (Language.tupled, Language.unapply)

  lazy val languagesTableQuery = TableQuery[LanguagesTable]
}

object LanguagesTable {
  lazy val languagesTableQuery = TableQuery[LanguagesTable]
}