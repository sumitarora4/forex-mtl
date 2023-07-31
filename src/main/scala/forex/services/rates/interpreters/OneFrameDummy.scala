package forex.services.rates.interpreters

import cats.Applicative
import cats.data.NonEmptyList
import forex.domain.Rate.Pair
import forex.domain.{Price, Rate, Timestamp}
import forex.services.rates.Algebra
import forex.services.rates.errors._
import cats.implicits._

class OneFrameDummy[F[_]: Applicative] extends Algebra[F] {

  override def get(pair: Rate.Pair): F[Error Either Rate] =
    Rate(pair, Price(BigDecimal(100)), Timestamp.now).asRight[Error].pure[F]

  def getAll(pairs: NonEmptyList[Pair]): F[Error Either NonEmptyList[Rate]] =
    for {
      results <- pairs.traverse(get)
      res = results.traverse(identity)
    } yield res
}