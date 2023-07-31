package forex.programs.rates

import cats.data.NonEmptyList
import forex.domain.Rate
import forex.domain.Rate.Pair
import forex.programs.rates.errors._

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._

trait Algebra[F[_]] {
  def get(request: Protocol.GetRatesRequest): F[Error Either Rate]

  def getAll(pairs: NonEmptyList[Pair]): F[errors.Error Either NonEmptyList[Rate]]

  def allRates: fs2.Stream[F, Rate] = fs2.Stream
    .eval(getAll(Rate.allCurrencyPairs))
    .filter(_.isRight)
    .map(
      // get is safe here, since it's guaranted right by previous filter operation
      _.right.get.toList
    )
    .flatMap(fs2.Stream.apply)

  /**
   * How much time to wait between invocation to one frame server given max invocations
   * they preserve?
   *
   * @param maxInvocations maximum served requests allowed for OneFrame server.
   * @return duration to wait for every update
   */
  def updateEvery(maxInvocations: Int): FiniteDuration =
    (60 * 60 * 24.0 / maxInvocations).seconds
}
