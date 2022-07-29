package models

import slick.jdbc.PostgresProfile.api._

case class AgeRating(id: Long = 0L, age_rating: String)

class AgeRatingsTable(tag: Tag) extends Table[AgeRating](tag, "age_ratings") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def age_rating = column[String]("age_rating")

  def * = (id, age_rating) <> (AgeRating.tupled, AgeRating.unapply)
}

object AgeRatingsTable {
  lazy val ageRatingsTableQuery = TableQuery[AgeRatingsTable]
}