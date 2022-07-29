package models

import slick.jdbc.PostgresProfile.api._

case class Comment(
                     id: Option[Long] = Some(0),
                     header: Option[String],
                     description: Option[String],
                     rate: Option[Int],
                     movie_id: Option[Long],
                     recommendation: Option[Boolean]
                  ) {
  def areAllAttributesDefined: Boolean = {
    header.isDefined &&
      description.isDefined &&
      rate.isDefined &&
      movie_id.isDefined &&
      recommendation.isDefined
  }
}

class CommentsTable(tag: Tag) extends Table[Comment](tag, "comments") {
  def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

  def header = column[Option[String]]("header")

  def description = column[Option[String]]("description")

  def rate = column[Option[Int]]("rate")

  def movie_id = column[Option[Long]]("movie_id")

  def recommendation = column[Option[Boolean]]("recommendation")

  def movie_id_fk = foreignKey(
    "movie_id_fk",
    column[Option[Long]]("movie_id"),
    TableQuery[MoviesTable]
  )(_.id)

  def * = (id, header, description, rate, movie_id, recommendation) <> (Comment.tupled, Comment.unapply)
}

object CommentsTable {
  lazy val commentsTableQuery = TableQuery[CommentsTable]
}