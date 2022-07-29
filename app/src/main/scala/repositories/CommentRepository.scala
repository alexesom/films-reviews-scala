package repositories

import com.typesafe.scalalogging.StrictLogging
import models.Comment
import models.CommentsTable.commentsTableQuery
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object CommentRepository extends StrictLogging {

  def insertComment(comment: Comment)(implicit db: Database): Future[Int] = {
    logger.debug(s"Inserting comment into db: $comment")
    Try {
      commentsTableQuery += comment
    } match {
      case Success(value) => db.run(value)

      case Failure(exception) =>
        Future.failed(exception)
    }
  }

  def getCommentsPage(page: Int, pageSize: Int, publicMovies: Boolean)(implicit db: Database): Future[Seq[Comment]] = {
    db.run(
      commentsTableQuery
        .take(page * pageSize)
        .result
    )
  }
}
