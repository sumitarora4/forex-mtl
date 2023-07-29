package forex.programs.rates

import forex.services.rates.errors.{Error => RatesServiceError}

object errors {

  sealed trait Error extends Exception
  object Error {
    final case class CurrencyNotSupported(requestedCurrency: Option[String]) extends Error
    final case object DoublePair extends Error
    final case class RateLookupFailed(msg: String) extends Error
  }

  def toProgramError(error: RatesServiceError): Error = error match {
    case RatesServiceError.CurrencyNotSupported(curr) => Error.CurrencyNotSupported(curr)
    case RatesServiceError.DoublePair                 => Error.DoublePair
    case RatesServiceError.OneFrameLookupFailed(msg) => Error.RateLookupFailed(msg)
  }
}
