import akka.http.scaladsl.Http
import models.json.{CommentJsonProtocol, MovieJsonProtocol}
import slick.jdbc.JdbcBackend.Database

import scala.io.StdIn

object Main extends MovieJsonProtocol with CommentJsonProtocol {

  import implicits.AkkaHttpImplicits._
  import routes.MainRoute._

  implicit val db = Database.forConfig("database")


  def main(args: Array[String]): Unit = {
    /*val scheduler = QuartzSchedulerTypedExtension(system)
    val movieUpdateActor = system.systemActorOf(Props[MovieUpdateActor],"fetchMoviesActor")

    scheduler.scheduleTyped("MovieFetchEveryDay9AM", )*/
    val server = Http().newServerAt("0.0.0.0", 8080).bind(routes)
    println("Hello world!")


    StdIn.readLine()
    server
      .flatMap(_.unbind())
      .onComplete(_ => {
        system.terminate()
      })
  }
}


