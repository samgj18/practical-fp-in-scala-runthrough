package shop.domains

import shop.domains.auth.UserId
import shop.domains.checkout.Card

import squants.market.Money
import derevo.derive
import derevo.circe.magnolia.decoder
import derevo.circe.magnolia.encoder

object payment {
  @derive(decoder, encoder)
  case class Payment(
      id: UserId,
      total: Money,
      card: Card
  )
}
