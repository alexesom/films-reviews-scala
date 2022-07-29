package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import models.User
import models.json.{CommentJsonProtocol, MovieJsonProtocol}
import routes.CommentRoute.{getCommentsPageRoute, getInsertCommentRoute}
import routes.GenreRoute.{getAllGenresRoute, getGenreIdFindRoute, getGenresUpdateRoute}
import routes.MovieIMDbRoute.getInsertMovieByIdRoute
import routes.MovieRoute.{getDeleteByIMDbIdRoute, getInsertMovieRoute, getMoviesPageRoute}
import services.AuthenticatorService.{adminPassAuthenticator, userPassAuthenticator}
import slick.jdbc.JdbcBackend.Database


object MainRoute extends MovieJsonProtocol with CommentJsonProtocol {

  def routes(implicit db: Database): Route = concat(
    postRoute,
    getRoute,
    deleteRoute
    //updateRoute
  )

  def deleteRoute(implicit db: Database): Route = delete {
    pathPrefix("movies") {
      authenticateBasicAsync("films-reviews-api", adminPassAuthenticator) { user: User =>
        path("imdb" / Segment) {
          getDeleteByIMDbIdRoute
        }
      }
    }
  }

  def getRoute(implicit db: Database): Route = get {
    concat(
      pathPrefix("genres") {
        concat(
          path("id" / Segment) { genre =>
            getGenreIdFindRoute(genre)
          },
          path("all") {
            getAllGenresRoute
          }
        )
      },
      pathPrefix("movies") {
        concat(
          parameters("page".withDefault(1), "pageSize".withDefault(25), "publicMovies".withDefault(true)) {
            (page, pageSize, publicMovies) =>
              publicMovies match {
                case false =>
                  authenticateBasicAsync("films-reviews-api", adminPassAuthenticator) { user: User =>
                    getMoviesPageRoute(page, pageSize, publicMovies)
                  }
                case true =>
                  getMoviesPageRoute(page, pageSize, publicMovies)
              }
          }

        )
      },
      pathPrefix("comments") {
        parameters("page".withDefault(1), "pageSize".withDefault(25), "publicMovies".withDefault(true)) {
          (page, pageSize, publicMovies) =>
            getCommentsPageRoute(page, pageSize, publicMovies)
        }
      }
    )
  }

  def postRoute(implicit db: Database): Route = post {
    concat(
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
      },
      pathPrefix("genres") {
        authenticateBasicAsync("films-reviews-api", adminPassAuthenticator) { user: User =>
          concat(
            path("update") {
              parameters("language".withDefault("en-US")) { genreLanguage =>
                getGenresUpdateRoute(genreLanguage)
              }
            }
          )
        }
      },
      pathPrefix("comments") {
        authenticateBasicAsync("films-reviews-api", userPassAuthenticator) { user: User =>
          pathEndOrSingleSlash {
            getInsertCommentRoute
          }
        }
      }
    )
  }
}
