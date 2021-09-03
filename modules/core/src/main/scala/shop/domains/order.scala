package shop.domains

import java.util.UUID

import scala.util.control.NoStackTrace

import shop.domains.cart.Quantity
import shop.domains.item.ItemId

import derevo.cats.show
import derevo.circe.magnolia.{ decoder, encoder }
import derevo.derive
import io.estatico.newtype.macros.newtype
import squants.Money

object order {
  @derive(decoder, encoder, show)
  @newtype case class OrderId(uuid: UUID)

  @derive(decoder, encoder, show)
  @newtype case class PaymentId(uuid: UUID)

  @derive(decoder, encoder)
  case class Order(
      id: OrderId,
      pid: PaymentId,
      items: Map[ItemId, Quantity],
      total: Money
  )

  @derive(show)
  case object EmptyCartError extends NoStackTrace

  @derive(show)
  sealed trait OrderOrPaymentError extends NoStackTrace {
    def cause: String
  }

  @derive(show)
  case class OrderError(cause: String)   extends OrderOrPaymentError
  case class PaymentError(cause: String) extends OrderOrPaymentError
}
