# A local proxy for Forex rates

Build a local proxy for getting Currency Exchange Rates

**Requirements**

Forex is a simple application that acts as a local proxy for getting exchange rates.
It's a service that can be consumed by other internal services to get the exchange rate between a set of currencies,
so they don't have to worry about the specifics of third-party providers.

> An internal user of the application should be able to ask for an exchange rate between 2 given currencies, and get back a rate that is no older than 5 minutes old.
> The application should support at least 10.000 requests per day.

In practice, this should require the following 2 points:

1. Create a `live` interpreter for the `oneframe` service. This should consume the [one-frame API](https://hub.docker.com/r/paidyinc/one-frame).
2. Adapt the `rates` processes (if necessary) to make sure you cover the requirements of the use case, and work around possible limitations of the third-party provider.
3. Make sure the service's own API gets updated to reflect the changes made in points 1 & 2.

## Getting started

Build and publish the docker image of this project.
```bash
sbt docker:publishLocal
```
Once the image is published, start all of the required images.
```bash
docker-compose up
```
Try to get the conversion rate between Indonesian Rupiah and Japanese Yen.
```bash
curl 'localhost:9090/rates?from=IDR&to=JPY'
```
What about, asking for all of the possible permutations of exchange rates?
```bash
curl 'localhost:9090/rates'
```
Firing up unit testing and integration testing.
```bash
sbt test
sbt it:test # make sure docker-compose up has executed beforehand
```

## Technology used

- [Cats-effect](https://typelevel.org/cats-effect/)
- [fs2](https://fs2.io/index.html)
- [http4s](https://fs2.io/index.html)
- [Weaver Test](https://disneystreaming.github.io/weaver-test/docs/multiple_suites_logging)
- [circe](https://circe.github.io/circe/parsing.html)

## Approach

`oneframe` service supports multiple pairs of queries in one `GET` request. Instead of asking for only one exchange rate, we can also ask for other rates at once like GBP to USD, JPY to AUD, etc.
To get the most benefits out of this, Forex will literally take every permutation of our supported currencies and caches all the rate results taken from `oneframe`.
Of course, this will only work if our supported currencies are minimal. Querying all of 22350 currency combinations in the world in a single GET to `oneframe` doesn't sound like a good plan,
but given that our server only supports 14 currencies, the permutation is only 182 and luckily still in the acceptable range of the `oneframe` server.

The main goals of Forex are two-fold:
- Overcome the limitations of 1000 invocations per day that the `oneframe` server gives.
- If local cache is used, it must be no older than 5 minutes.

If we call `oneframe` every 86.4 seconds starting early in the day, the 1000th call will be at the very end of the day. That is, Forex tries to call `oneframe` 1000 times in a day by waiting 86.4 seconds
between each calls, hence the cache age wouldn't be older than 86.4 seconds. And yes, we call `oneframe` greedily to update every currency combinations within each call :)

The scheduler to update the cache is implemented using `fs2`.
## Code practices and structures

The initiator of this project used typelevel stacks and aimed to be more using scala in functional way. Aligned with this initiative, this project tries to follow functional programming principles
by avoiding side effects and impurity. Every impure expression will be wrapped inside an `IO` construct.

## Room for improvements

- The discussed approach is only working if the supported currencies are minimal.
- Retry logic when calling `oneframe` API.
- More testing, or if possible, apply testing based on algebraic laws.
- OpenAPI specifications, probably using [tAPIr](https://tapir.softwaremill.com/en/latest/).
- Streamify the initialization process. Currently we call `.compile.drain` twice. We can simplify it to become one.
- Simulation testing will verify that Forex never exceeds 1000 invocations (a `oneframe` constraint). A fake full day test ought to be implemented by supplying fake timer.