package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import models.User
import routes.modelsRoutes.MovieRoute.getDeleteByIMDbIdRoute
import services.AuthenticatorService.adminPassAuthenticator
import slick.jdbc.JdbcBackend.Database


object DeleteRoute {
  def moviesPathDeleteRoute(implicit db: Database): Route = {
    pathPrefix("movies") {
      authenticateBasicAsync("films-reviews-api", adminPassAuthenticator) { user: User =>
        path("imdb" / Segment) {
          getDeleteByIMDbIdRoute
        }
      }
    }
  }
}
