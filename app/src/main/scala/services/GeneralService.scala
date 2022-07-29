package services

import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives.{as, entity, provide}
import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller

object GeneralService {
  def optionalEntity[T](unmarshaller: FromRequestUnmarshaller[T]): Directive1[Option[T]] = {
    entity(as[String]).flatMap { stringEntity =>
      if (stringEntity == null || stringEntity.isEmpty) {
        provide(Option.empty[T])
      } else {
        entity(unmarshaller).flatMap(e => provide(Some(e)))
      }
    }
  }
}
