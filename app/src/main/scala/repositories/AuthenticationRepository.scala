package repositories

import models.User
import models.UsersTable.usersTableQuery
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

object AuthenticationRepository {
  def fetchUser(login: String)(implicit db: Database): Future[Seq[User]] = {
    val action = usersTableQuery.filter(_.login === login).result

    db.run(action)
  }
}
