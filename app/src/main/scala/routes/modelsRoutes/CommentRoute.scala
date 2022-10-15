package routes.modelsRoutes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCode}
import akka.http.scaladsl.server.Directives.{as, complete, onComplete}
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.StrictLogging
import io.circe.syntax.EncoderOps
import models.Comment
import models.json.CommentJsonProtocol
import repositories.CommentRepository.{getCommentsPage, insertComment}
import services.GeneralService.optionalEntity
import slick.jdbc.JdbcBackend.Database
import spray.json.enrichAny

import scala.util.{Failure, Success}

object CommentRoute extends CommentJsonProtocol with StrictLogging {
  def getInsertCommentRoute(implicit db: Database): Route = {
    optionalEntity(as[Comment]) {
      case Some(comment) =>
        if (comment.areAllAttributesDefined) { //movie id verification TODO
          getCommentFulfilledInsertRoute(comment)
        } else {
          complete(
            StatusCode.int2StatusCode(400),
            HttpEntity(ContentTypes.`application/json`,
              """{"Error":"Not all fields defined."}"""))
        }
      case _ =>
        complete(
          StatusCode.int2StatusCode(400),
          HttpEntity(
            ContentTypes.`application/json`,
            """{"Error":"Incorrect JSON provided."}""")

        )
    }
  }

  def getCommentFulfilledInsertRoute(comment: Comment)(implicit db: Database): Route = {
    logger.debug(s"Trying to insert fulfilled comment into database: $comment")

    onComplete(insertComment(comment)) {
      case Success(_) =>
        complete(
          HttpEntity(
            ContentTypes.`application/json`,
            comment.toJson.toString)
        )

      case Failure(exception) =>
        logger.debug(s"Comment insertion failed: ${comment.toString}," +
          s"exception message: ${exception.getMessage.replace("\"", "\'")}}," +
          s"trace: ${exception.getStackTrace.mkString("Array(", ", ", ")")}")

        complete(StatusCode.int2StatusCode(400),
          HttpEntity(ContentTypes.`application/json`,
            s"""{
               |"Error":"Comment insertion failed.",
               |"Exception": "${exception.getMessage.replace("\"", "\'")}",
               |"Trace": "${exception.getStackTrace.mkString("Array(", ", ", ")")}"
               |}""".stripMargin
          )
        )
    }
  }

  def getCommentsPageRoute(page: Int, pageSize: Int, publicMovies: Boolean)(implicit db: Database): Route = {
    onComplete(getCommentsPage(page, pageSize, publicMovies)) {
      case Success(commentsSeq) =>
        complete(
          StatusCode.int2StatusCode(200),
          HttpEntity(
            ContentTypes.`application/json`,
            commentsSeq.asJson.toString()
          )
        )
      case Failure(exception) =>
        complete(
          StatusCode.int2StatusCode(400),
          HttpEntity(
            ContentTypes.`application/json`,
            s"""{
               |"Error": "Error occurred while getting comments by page: $page, pageSize: $pageSize, publicMovies: $publicMovies",
               |"Exception": "${exception.getMessage.replace("\"", "\'")}",
               |"Trace": "${exception.getStackTrace.mkString("Array(", ", ", ")")}"
               |}""".stripMargin
          )
        )
    }
  }
}
