package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import services.MovieService.fetchAllMoviesFromTMDb

object MovieUpdateActor {
  final case class FetchMoviesMessage()

  def apply(): Behavior[FetchMoviesMessage] =
    Behaviors.setup(context => new MovieUpdateActor(context))
}

class MovieUpdateActor(context: ActorContext[MovieUpdateActor.FetchMoviesMessage])
  extends AbstractBehavior[MovieUpdateActor.FetchMoviesMessage](context) {

  import MovieUpdateActor._

  override def onMessage(message: FetchMoviesMessage): Behavior[FetchMoviesMessage] = {
    fetchAllMoviesFromTMDb
    this
  }
}
