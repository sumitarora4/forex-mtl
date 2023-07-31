package forex.services.rates.interpreters

import forex.domain.Currency
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import forex.domain.Rate.Pair

package object http {

  object currencies {
    val currencyPermutations = Currency.allCombinations.toList.toSet
    val validCurrencyPairs   = Gen.oneOf(currencyPermutations)
    val validPairsGen        = validCurrencyPairs.map { case (c1, c2) => Pair(c1, c2) }
    val validPairListGen     = Gen.nonEmptyListOf(validPairsGen)
    val currencyGen          = Gen.oneOf(Currency.values)

    implicit val currencyArb     = Arbitrary(currencyGen)
    implicit val currencyPairArb = Arbitrary(validCurrencyPairs)
    implicit val pairArb         = Arbitrary(validPairsGen)
    implicit val pairListArb     = Arbitrary(validPairListGen)
  }
}
