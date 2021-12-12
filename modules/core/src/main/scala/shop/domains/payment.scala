package shop.domains

import shop.domains.auth.UserId
import shop.domains.checkout.Card

import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive
import squants.market.Money

object payment {
  @derive(decoder, encoder)
  case class Payment(
      id: UserId,
      total: Money,
      card: Card
  )
}
