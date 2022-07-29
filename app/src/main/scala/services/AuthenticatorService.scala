package services

import akka.http.scaladsl.server.directives.Credentials
import com.google.common.hash.Hashing
import models.User
import slick.jdbc.JdbcBackend.Database

import java.nio.charset.StandardCharsets
import scala.concurrent.Future

object AuthenticatorService {

  import implicits.AkkaHttpImplicits._
  import repositories.AuthenticationRepository._

  def userPassAuthenticator(credentials: Credentials)(implicit db: Database): Future[Option[User]] = {
    credentials match {
      case p@Credentials.Provided(login) =>
        val userFutureOption = fetchUser(login)
          .map(_.headOption)

        userFutureOption.map {
          case userOption@Some(user) =>
            if (
              p.verify(
                user.password,
                secret => Hashing.sha256().hashString(secret, StandardCharsets.UTF_8).toString
              ) && !user.isAdmin
            ) {
              userOption
            } else
              None
        }

      case _ => Future.successful(None)
    }
  }

  def adminPassAuthenticator(credentials: Credentials)(implicit db: Database): Future[Option[User]] = {
    val userFutureOption = credentials match {
      case p@Credentials.Provided(login) =>
        val userFutureOption = fetchUser(login)
          .map(_.headOption)

        userFutureOption.map {
          case userOption@Some(user) =>
            if (
              p.verify(
                user.password,
                secret => Hashing.sha256().hashString(secret, StandardCharsets.UTF_8).toString
              ) && user.isAdmin
            ) {
              userOption
            } else
              None
        }

      case _ => Future.successful(None)
    }

    userFutureOption.map(userOption => userOption.map(_.isAdmin) match {
      case Some(true) => userOption
      case Some(false) => None
      case _ => None
    }
    )
  }
}
