package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import models.User
import routes.modelsRoutes.CommentRoute.getCommentsPageRoute
import routes.modelsRoutes.GenreRoute.{getAllGenresRoute, findGenreByGenreIdRoute}
import routes.modelsRoutes.MovieRoute.getMoviesPageRoute
import services.AuthenticatorService.adminPassAuthenticator
import slick.jdbc.JdbcBackend.Database


object GetRoute {
  def commentsPathGetRoute(implicit db: Database): Route = {
    pathPrefix("comments") {
      parameters("page".withDefault(1), "pageSize".withDefault(25), "publicMovies".withDefault(true)) {
        (page, pageSize, publicMovies) =>
          getCommentsPageRoute(page, pageSize, publicMovies)
      }
    }
  }

  def moviesPathGetRoute(implicit db: Database): Route = {
    pathPrefix("movies") {
      concat(
        parameters(
          "page".withDefault(1),
          "pageSize".withDefault(25),
          "publicMovies".withDefault(true)) {
          (page, pageSize, publicMovies) =>
            if (publicMovies) {
              getMoviesPageRoute(page, pageSize, publicMovies)
            } else {
              authenticateBasicAsync("films-reviews-api", adminPassAuthenticator) { user: User =>
                getMoviesPageRoute(page, pageSize, publicMovies)
              }
            }
        }
      )
    }
  }

  def genresPathGetRoute(implicit db: Database): Route = {
    pathPrefix("genres") {
      concat(
        path("id" / Segment) { genre =>
          findGenreByGenreIdRoute(genre)
        },
        path("all") {
          getAllGenresRoute
        }
      )
    }
  }
}
