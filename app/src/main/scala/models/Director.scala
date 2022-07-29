package models

import slick.jdbc.PostgresProfile.api._
import java.time.LocalDate

case class Director(id: Long = 0L, firstName: String, secondName: String, dob: LocalDate)

class DirectorsTable(tag: Tag) extends Table[Director](tag, "directors") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def firstName = column[String]("firstName")

  def secondName = column[String]("secondName")

  def dob = column[LocalDate]("dob")

  def * = (id, firstName, secondName, dob) <> (Director.tupled, Director.unapply)
}

object DirectorsTable {
  lazy val directorsTableQuery = TableQuery[DirectorsTable]
}