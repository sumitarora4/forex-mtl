package forex.programs.rates

import forex.domain.Rate
import forex.programs.rates.errors._

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._

trait Algebra[F[_]] {
  def get(request: Protocol.GetRatesRequest): F[Error Either Rate]

  def allRates: fs2.Stream[F, Rate]

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
