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