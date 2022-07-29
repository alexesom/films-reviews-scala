package repositories

import com.typesafe.scalalogging.StrictLogging
import models.{Movie, MoviesTable}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object MovieRepository extends StrictLogging {

  import MoviesTable._
  import implicits.AkkaHttpImplicits._

  def deleteMovieByIMDbId(imdbId: String)(implicit db: Database): Future[Option[Movie]] = {
    val imdbIdFilterQuery = moviesTableQuery
      .filter(_.imdbId === imdbId)

    val movieFuture = db
      .run(imdbIdFilterQuery.result)
      .map(_.headOption)

    db.run(imdbIdFilterQuery.delete)

    movieFuture
  }

  def insertMovie(movie: Movie)(implicit db: Database): Future[Int] = {
    logger.debug(s"Inserting movie into db: $movie")
    Try {
      moviesTableQuery += movie
    } match {
      case Success(value) => db.run(value)

      case Failure(exception) =>
        Future.failed(exception)
    }
  }

  def getMoviesPage(page: Int, pageSize: Int, publicMovies: Boolean)(implicit db: Database): Future[Seq[Movie]] = {
    db.run(
      moviesTableQuery
        .filter(_.isPublic === publicMovies)
        .take(page * pageSize)
        .result
    )
  }
}
