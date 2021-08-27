package shop.domains

import java.util.UUID

import shop.domains.cart.Quantity
import shop.domains.item.ItemId

import io.estatico.newtype.macros.newtype
import squants.Money

object order {
  @newtype case class OrderId(uuid: UUID)
  @newtype case class PaymentId(uuid: UUID)

  case class Order(
      id: OrderId,
      pid: PaymentId,
      items: Map[ItemId, Quantity],
      total: Money
  )
}
