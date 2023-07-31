package forex.http.rates

import cats.Show
import forex.domain.Currency.show
import forex.domain.Rate.Pair
import forex.domain._
import forex.programs.rates.errors
import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.QueryParamDecoder
import org.http4s.dsl.impl.QueryParamDecoderMatcher

object Protocol {

  implicit val configuration: Configuration = Configuration.default.withSnakeCaseMemberNames

  final case class GetApiRequest(
      from: Currency,
      to: Currency
  )

  implicit lazy val GetApiResponseShow = Show.show[GetApiResponse](p => s"${p.from}${p.to}${p.price}${p.timestamp}")
  implicit lazy val RateShow =  { Show.show[Rate](p => s"${p.pair}${p.price}${p.timestamp}")}

  final case class GetApiResponse(
      from: Currency,
      to: Currency,
      price: Price,
      timestamp: Timestamp
  )

  implicit val currencyEncoder: Encoder[Currency] =
    Encoder.instance[Currency] { show.show _ andThen Json.fromString }

  implicit val pairEncoder: Encoder[Pair] =
    deriveEncoder[Pair]

  implicit val rateEncoder: Encoder[Rate] =
    deriveEncoder[Rate]

  implicit val responseEncoder: Encoder[GetApiResponse] =
    deriveEncoder[GetApiResponse]

  private[http] implicit val currencyParam = QueryParamDecoder[String].map { currName =>
    import cats.implicits._
    Currency.fromString(currName).leftMap(errors.toProgramError)
  }

  object FromParam extends QueryParamDecoderMatcher[errors.Error Either Currency]("from")
  object ToParam extends QueryParamDecoderMatcher[errors.Error Either Currency]("to")

}
