package shop.domains

import java.util.UUID
import scala.util.control.NoStackTrace

import shop.domains.auth._
import shop.domains.item.{ Item, ItemId }
import shop.infrastructure.optics.uuid

import derevo.cats._
import derevo.circe.magnolia.{ decoder, encoder }
import derevo.derive
import io.circe.{ Decoder, Encoder }
import io.estatico.newtype.macros.newtype
import squants.market.Money

object cart {

  /**
    * eqv ->
    * A type class used to determine equality between 2 instances of the same
    * type. Any 2 instances `x` and `y` are equal if `eqv(x, y)` is `true`.
    * Moreover, `eqv` should form an equivalence relation.
    */
  @derive(decoder, encoder, eqv, show)
  @newtype case class Quantity(value: Int)

  @derive(eqv, show)
  @newtype case class Cart(items: Map[ItemId, Quantity])

  @derive(decoder, encoder, eqv, show, uuid)
  @newtype
  case class CartId(value: UUID)

  object Cart {
    implicit val jsonEncoder: Encoder[Cart] =
      Encoder.forProduct1("items")(_.items)

    implicit val jsonDecoder: Decoder[Cart] =
      Decoder.forProduct1("items")(Cart.apply)
  }

  @derive(decoder, encoder)
  case class CartItem(item: Item, quantity: Quantity)

  @derive(decoder, encoder)
  case class CartTotal(items: List[CartItem], total: Money)

  @derive(decoder, encoder)
  case class CartNotFound(userId: UserId) extends NoStackTrace
}
