package forex.config

import cats.effect.Sync
import forex.config.config.ApplicationConfig
import fs2.Stream
import pureconfig.ConfigSource
import pureconfig.ConfigReader
import pureconfig.error.CannotConvert
import org.http4s.Uri
import org.http4s.ParseFailure


object Config {

  /**
   * @param path the property path inside the default configuration
   */
  def stream[F[_]: Sync](path: String): Stream[F, ApplicationConfig] = {
    import pureconfig.generic.auto._
    Stream.eval(Sync[F].delay(
      ConfigSource.default.at(path).loadOrThrow[ApplicationConfig]))
  }

  import cats.implicits._

  implicit val uriReader: ConfigReader[Uri] = ConfigReader[String].emap { url =>
    Uri.fromString(url).leftMap {
      case ParseFailure(msg, _) => CannotConvert(url, "Uri", msg)
    }
  }

}
