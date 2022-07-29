package models

import slick.jdbc.PostgresProfile.api._

case class Country(id: Long = 0L, country: String)

class CountriesTable(tag: Tag) extends Table[Country](tag, "countries") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def country = column[String]("country")

  def * = (id, country) <> (Country.tupled, Country.unapply)
}

object CountriesTable {
  lazy val countriesTableQuery = TableQuery[CountriesTable]
}