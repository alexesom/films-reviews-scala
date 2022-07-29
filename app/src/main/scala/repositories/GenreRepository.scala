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
        .filter(_.genre === genreName)
        .result

    db.run(findGenreAction)
      .map(genreSeq => genreSeq.headOption)
  }

  def updateGenres(genresSeq: Seq[Genre])(implicit db: Database): Future[Option[Int]] = {
    import models.GenresTable.genresTableQuery

    val genresNamesExistFuture = db.run(genresTableQuery.map(_.genre).result)

    val genresSeqFilterNotExistFuture =
      genresNamesExistFuture.map(genresNamesExist =>
        genresSeq.filterNot(
          genresNamesExist
            .map(genreName => Genre(Some(0), genreName))
            .toSet
        )
      )

    genresSeqFilterNotExistFuture
      .map(genresSeqFilterNotExist => genresTableQuery ++= genresSeqFilterNotExist)
      .flatMap(genreAction => db.run(genreAction))
  }

  def getAllGenres(implicit db: Database): Future[Seq[Genre]] = {
    db.run(genresTableQuery.result)
  }
}
