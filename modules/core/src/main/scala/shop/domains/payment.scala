package shop.domains

import shop.domains.auth.UserId
import shop.domains.checkout.Card

import squants.market.Money

object payment {
  case class Payment(
      id: UserId,
      total: Money,
      card: Card
  )
}
