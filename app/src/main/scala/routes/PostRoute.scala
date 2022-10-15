package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import models.User
import routes.modelsRoutes.CommentRoute.getInsertCommentRoute
import routes.modelsRoutes.GenreRoute.updateGenresRoute
import routes.modelsRoutes.MovieIMDbRoute.getInsertMovieByIdRoute
import routes.modelsRoutes.MovieRoute.getInsertMovieRoute
import services.AuthenticatorService.{adminPassAuthenticator, userPassAuthenticator}
import slick.jdbc.JdbcBackend.Database

object PostRoute {
  def commentsPathPostRoute(implicit db: Database): Route = {
    pathPrefix("comments") {
      authenticateBasicAsync("films-reviews-api", userPassAuthenticator) { user: User =>
        pathEndOrSingleSlash {
          getInsertCommentRoute
        }
      }
    }
  }

  def genresPathPostRoute(implicit db: Database): Route = {
    pathPrefix("genres") {
      authenticateBasicAsync("films-reviews-api", adminPassAuthenticator) { user: User =>
        concat(
          path("update") {
            parameters("language".withDefault("en-US")) { genreLanguage =>
              updateGenresRoute(genreLanguage)
            }
          }
        )
      }
    }
  }

  def moviesPathPostRoute(implicit db: Database): Route = {
    pathPrefix("movies") {
      authenticateBasicAsync("films-reviews-api", adminPassAuthenticator) { user: User =>
        concat(
          pathEndOrSingleSlash {
            getInsertMovieRoute
          },
          path("imdb" / Segment) { movieId: String =>
            parameters("isPublic".withDefault(false)) { isPublicValue =>
              getInsertMovieByIdRoute(movieId, isPublicValue)
            }
          }
        )
      }
    }
  }
}
