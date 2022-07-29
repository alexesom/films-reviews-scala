package services

import java.time.LocalDate
import scala.util.{Failure, Success, Try}

object ConverterService {
  def parseJsonStringToLocalDate(stringDate: String): LocalDate = {
    Try {
      stringDate.toInt
    } match {
      case Success(year) => LocalDate.of(year, 1, 1)
      case Failure(_) => Try {
        import java.time.format.DateTimeFormatter

        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
        LocalDate.parse(stringDate, formatter)
      } match {
        case Success(value) => value
        case Failure(_) => LocalDate.MIN
      }
    }
  }
}
