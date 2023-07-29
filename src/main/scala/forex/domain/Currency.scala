package forex.domain

import cats.Show
import enumeratum._

sealed trait Currency extends EnumEntry

object Currency extends Enum[Currency] with CatsEnum[Currency]{

  val values = findValues

  case object AUD extends Currency
  case object CAD extends Currency
  case object CHF extends Currency
  case object EUR extends Currency
  case object GBP extends Currency
  case object NZD extends Currency
  case object JPY extends Currency
  case object SGD extends Currency
  case object USD extends Currency

  implicit val show: Show[Currency] = Show.show {
    case AUD => "AUD"
    case CAD => "CAD"
    case CHF => "CHF"
    case EUR => "EUR"
    case GBP => "GBP"
    case NZD => "NZD"
    case JPY => "JPY"
    case SGD => "SGD"
    case USD => "USD"
  }

  import cats.implicits._
  import forex.services.rates.errors
  import forex.services.rates.errors.Error.CurrencyNotSupported

  def fromString(currencyName: String): errors.Error Either Currency =
    Currency.withNameInsensitiveEither(currencyName).leftMap {
      case _ => CurrencyNotSupported(currencyName.some)
    }
}
