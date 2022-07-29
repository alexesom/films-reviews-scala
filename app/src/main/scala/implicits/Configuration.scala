package implicits

import com.typesafe.config.{Config, ConfigFactory}

object Configuration {
  implicit val config: Config = ConfigFactory.load()

  implicit val uriOMDb: String = config.getString("api.OMDb.uri")
  implicit val apiKeyOMDb: String = config.getString("api.OMDb.apiKey")
  implicit val uriTMDb: String = config.getString("api.TMDb.uri")
  implicit val apiKeyTMDb: String = config.getString("api.TMDb.apiKey")
  implicit val genresUpdateUriPart: String = config.getString("api.TMDb.genresUpdateUriPart")
}
