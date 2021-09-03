package shop.infrastructure.http

import java.util.UUID

import shop.domains.item._
import shop.domains.order._

import cats.implicits._

object vars {

  protected class UUIDVar[A](f: UUID => A) {
    def unapply(str: String): Option[A] =
      Either.catchNonFatal(f(UUID.fromString(str))).toOption
  }

  object ItemIdVar  extends UUIDVar(ItemId.apply)
  object OrderIdVar extends UUIDVar(OrderId.apply)
}
