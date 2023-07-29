package forex.http.rates

import cats.effect.Sync
import cats.implicits.catsSyntaxApplicativeError
import cats.syntax.flatMap._
import forex.programs.RatesProgram
import forex.programs.rates.{Protocol => RatesProgramProtocol}
import io.chrisdavenport.log4cats.Logger
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

class RatesHttpRoutes[F[_]: Sync: Logger](rates: RatesProgram[F]) extends Http4sDsl[F] {

  import Converters._
  import Protocol._
  import QueryParams._
  import forex.programs.rates.errors.Error

  private[http] val prefixPath = "/rates"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root :? FromQueryParam(from) +& ToQueryParam(to) =>

      val result = rates.get(RatesProgramProtocol.GetRatesRequest(from, to))
           .flatMap(Sync[F].fromEither)
           .flatMap { rate =>
        Ok(rate.asGetApiResponse)
      }

      result.handleErrorWith {
        case Error.CurrencyNotSupported(curr) =>
          BadRequest(s"Currency ${curr.getOrElse("")} is not supported.")
        case Error.DoublePair =>
          BadRequest(s"Pair must have different currencies")
        case Error.RateLookupFailed(msg) =>
          Logger[F].error(s"Internal error: $msg") >> InternalServerError(s"Rate lookup failed: $msg")
        case err =>
          Logger[F].error(s"Internal error: ${err.getMessage}") >> InternalServerError()
      }
  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )

}
