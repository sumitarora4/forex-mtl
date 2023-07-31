package forex.http.rates

import forex.domain.Currency
import forex.programs.rates.errors
import org.http4s.QueryParamDecoder
import org.http4s.dsl.impl.QueryParamDecoderMatcher

object QueryParams {

  private[http] implicit val currencyQueryParam = QueryParamDecoder[String].map { currName =>
    import cats.implicits._
    Currency.fromString(currName).leftMap(errors.toProgramError)
  }

  object FromQueryParam extends QueryParamDecoderMatcher[errors.Error Either Currency]("from")
  object ToQueryParam extends QueryParamDecoderMatcher[errors.Error Either Currency]("to")

}
