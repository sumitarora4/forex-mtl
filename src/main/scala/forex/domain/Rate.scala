package forex.domain

import cats.Show

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
}
