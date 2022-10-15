package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import models.json.{CommentJsonProtocol, MovieJsonProtocol}
import slick.jdbc.JdbcBackend.Database


object MainRoute extends MovieJsonProtocol with CommentJsonProtocol {
  import DeleteRoute._
  import GetRoute._
  import PostRoute._

  def routes(implicit db: Database): Route = concat(
    getRoute,
    postRoute,
    deleteRoute
  )

  def getRoute(implicit db: Database): Route = get {
    concat(
      genresPathGetRoute,
      moviesPathGetRoute,
      commentsPathGetRoute
    )
  }

  def postRoute(implicit db: Database): Route = post {
    concat(
      moviesPathPostRoute,
      genresPathPostRoute,
      commentsPathPostRoute
    )
  }

  def deleteRoute(implicit db: Database): Route = delete {
    moviesPathDeleteRoute
  }
}
