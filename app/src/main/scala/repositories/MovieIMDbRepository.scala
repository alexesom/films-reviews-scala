package repositories

import com.typesafe.scalalogging.StrictLogging
import models.AgeRatingsTable.ageRatingsTableQuery
import models.CountriesTable.countriesTableQuery
import models.{AgeRating, Country, Language}
import models.LanguagesTable.languagesTableQuery
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object MovieIMDbRepository extends StrictLogging {
  import implicits.AkkaHttpImplicits._

  def getAgeRatingIdByRating(ageRate: String)(implicit db: Database): Future[Long] = {

    val findOccurrenceAction = ageRatingsTableQuery
      .filter(_.age_rating.like(ageRate.trim))
      .map(_.id)
      .take(1)
      .result

    db.run(findOccurrenceAction)
      .map(_.headOption)
      .flatMap {
        case Some(ageRateId) => Future {
          ageRateId
        }
        case None => insertAgeRate(ageRate)
      }
  }

  def getLanguageIdByLanguage(language: String)(implicit db: Database): Future[Long] = {

    val findOccurrenceAction = languagesTableQuery
      .filter(_.language.like(language.trim))
      .map(_.id)
      .take(1)
      .result

    db.run(findOccurrenceAction)
      .map(_.headOption)
      .flatMap {
        case Some(languageId) => Future {
          languageId
        }
        case None => insertLanguage(language)
      }
  }

  def getCountryIdByCountry(country: String)(implicit db: Database): Future[Long] = {

    val findOccurrenceAction = countriesTableQuery
      .filter(_.country.like(country.trim))
      .map(_.id)
      .take(1)
      .result

    db.run(findOccurrenceAction)
      .map(_.headOption)
      .flatMap {
        case Some(countryId) => Future {
          countryId
        }
        case None => insertCountry(country)
      }
  }

  def insertAgeRate(ageRate: String)(implicit db: Database): Future[Long] = {
    logger.debug(s"Inserting age rate into db because of non-existing: $ageRate")
    Try {
      ageRatingsTableQuery += AgeRating(id = 0, ageRate)
    } match {
      case Success(action) => db.run(action).map(_.toLong)

      case Failure(exception) =>
        Future.failed(exception)
    }
  }

  def insertLanguage(language: String)(implicit db: Database): Future[Long] = {
    logger.debug(s"Inserting language into db because of non-existing: $language")
    Try {
      val languagesIndexReturning = languagesTableQuery returning languagesTableQuery.map(_.id)
      languagesIndexReturning += Language(id = 0, language)
    } match {
      case Success(action) => db.run(action)

      case Failure(exception) =>
        Future.failed(exception)
    }
  }

  def insertCountry(country: String)(implicit db: Database): Future[Long] = {
    logger.debug(s"Inserting age rate into db because of non-existing: $country")
    Try {
      val countriesIndexReturning = countriesTableQuery returning countriesTableQuery.map(_.id)
      countriesIndexReturning += Country(id = 0, country)
    } match {
      case Success(action) => db.run(action)

      case Failure(exception) =>
        Future.failed(exception)
    }
  }
}
