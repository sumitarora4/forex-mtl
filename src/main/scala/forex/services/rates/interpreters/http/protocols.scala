package forex.services.rates.interpreters.http

import cats.implicits._
import forex.domain.{Currency, Rate}
import forex.services.rates.errors
import io.circe.Decoder

import java.time.OffsetDateTime

object protocols {

  import io.circe.generic.semiauto._

  final case class RateResponse(
      from: Currency,
      to: Currency,
      bid: BigDecimal,
      ask: BigDecimal,
      price: BigDecimal,
      time_stamp: OffsetDateTime
  ) {
    def toDomain: errors.Error Either Rate = Rate.create(from, to, price, time_stamp)
  }

  final case class ErrorResponse(error: String) {
    def toDomain: errors.Error =
      error match {
        case "Invalid Currency Pair" => errors.Error.CurrencyNotSupported()
        case "Double Pair"           => errors.Error.DoublePair
        case msg                     => errors.Error.OneFrameLookupFailed(msg)
      }
  }

  implicit val currencyDec: Decoder[Currency] = Decoder[String].emap { currName =>
    Currency.fromString(currName).leftMap(_ => s"Currency $currName is not supported")
  }

  implicit val rateResponseDec: Decoder[RateResponse] = deriveDecoder[RateResponse]
  implicit val errResponseDec: Decoder[ErrorResponse] = deriveDecoder[ErrorResponse]
}
