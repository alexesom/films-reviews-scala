package repositories

import models.Genre
import models.GenresTable.genresTableQuery
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

object GenreRepository {

  import implicits.AkkaHttpImplicits._

  def getGenreByGenreId(genreName: String)(implicit db: Database): Future[Option[Genre]] = {
    val findGenreAction =
      genresTableQuery
        .filter(_.name === genreName)
        .result

    db.run(findGenreAction)
      .map(genreSeq => genreSeq.headOption)
  }

  def updateGenres(fetchedGenres: Seq[Genre])(implicit db: Database): Future[Option[Int]] = {
    import models.GenresTable.genresTableQuery

    val fetchedGenresNames = fetchedGenres.map(_.name)

    val existingGenresNamesFuture = db.run(genresTableQuery.map(_.name).result)
    val missingGenresNamesFuture = existingGenresNamesFuture
      .map(existingGenresNames =>
        fetchedGenresNames
          .filterNot(existingGenresNames.toSet)
      )
    val missingGenresFuture = missingGenresNamesFuture
      .map(
        _.map(Genre(0, _))
      )

    missingGenresFuture.flatMap(missingGenres => db.run(genresTableQuery ++= missingGenres))
  }

  def getAllGenres(implicit db: Database): Future[Seq[Genre]] = {
    db.run(genresTableQuery.result)
  }
}
