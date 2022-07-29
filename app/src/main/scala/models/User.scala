package models

import slick.jdbc.PostgresProfile.api._

case class User(
                 id: Long = 0L,
                 firstName: Option[String],
                 secondName: Option[String],
                 login: String,
                 password: String,
                 isAdmin: Boolean
               )

class UsersTable(tag: Tag) extends Table[User](tag, "users") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def firstName = column[Option[String]]("first_name")
  def secondName = column[Option[String]]("second_name")
  def login = column[String]("login", O.Unique)
  def password = column[String]("password")
  def isAdmin = column[Boolean]("is_admin")

  def * = (id, firstName, secondName, login, password, isAdmin) <> (User.tupled, User.unapply)

  lazy val usersTableQuery = TableQuery[UsersTable]
}

object UsersTable {
  lazy val usersTableQuery = TableQuery[UsersTable]
}
