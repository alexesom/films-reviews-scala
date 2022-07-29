package models.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import models.Comment
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait CommentJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val jsonSupportComment: RootJsonFormat[Comment] = jsonFormat6(Comment)
  implicit val commentEncoder: Encoder[Comment] = deriveEncoder[Comment]
}
