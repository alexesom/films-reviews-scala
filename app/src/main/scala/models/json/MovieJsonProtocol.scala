package models.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import models.Movie
import services.ConverterService.parseJsonStringToLocalDate
import spray.json.{DefaultJsonProtocol, JsString, JsValue, JsonFormat, RootJsonFormat}

import java.time.LocalDate

trait BaseMovieJsonProtocol extends DefaultJsonProtocol {
  implicit val localDateFormat: JsonFormat[LocalDate] = new JsonFormat[LocalDate] {
    override def write(obj: LocalDate): JsValue = JsString(obj.toString)

    override def read(json: JsValue): LocalDate = json match {
      case date: JsString => parseJsonStringToLocalDate(date.value)
      case _ =>
        throw new IllegalArgumentException(
          s"Can not parse json value [$json] to a local date object")
    }
  }
}

trait MovieJsonProtocol extends SprayJsonSupport with BaseMovieJsonProtocol {
  implicit val jsonSupportMovie: RootJsonFormat[Movie] = jsonFormat13(Movie)
  implicit val movieEncoder: Encoder[Movie] = deriveEncoder[Movie]
}
