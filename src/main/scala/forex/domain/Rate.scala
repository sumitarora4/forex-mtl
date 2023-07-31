package forex.domain

import cats.Show
import cats.data.NonEmptyList
import cats.implicits._
import java.time.OffsetDateTime
import forex.services.rates.errors

case class Rate(
    pair: Rate.Pair,
    price: Price,
    timestamp: Timestamp
)

object Rate {

  implicit val pairShow = Show.show[Pair](p => s"${p.from}${p.to}")

  final case class Pair(
      from: Currency,
      to: Currency
  )

  def create(
              from: Currency,
              to: Currency,
              price: BigDecimal,
              timeStamp: OffsetDateTime
            ): errors.Error Either Rate =
    pairCreate(from, to).map { pair =>
      Rate(pair, Price(price), Timestamp(timeStamp))
    }

  def pairCreate(from: Currency, to: Currency): errors.Error Either Pair =
    if (from == to) errors.Error.DoublePair.asLeft
    else Pair(from, to).asRight

  def allCurrencyPairs: NonEmptyList[Pair] =
    Currency.allCombinations.map { case (c1, c2) => Pair(c1, c2) }
}
