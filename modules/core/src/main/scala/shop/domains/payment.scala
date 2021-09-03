package shop.domains

import shop.domains.auth.UserId

import squants.market.Money

object payment {
  case class Payment(
      id: UserId,
      total: Money,
      card: Card
  )

  // dumb so it compiles
  case class Card()
}
