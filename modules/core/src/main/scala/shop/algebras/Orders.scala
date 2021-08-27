package shop.algebras

import shop.domains.auth.UserId
import shop.domains.cart.CartItem
import shop.domains.order.{ Order, OrderId, PaymentId }

import cats.data.NonEmptyList
import squants.Money

trait Orders[F[_]] {
  def get(
      userId: UserId,
      orderId: OrderId
  ): F[Option[Order]]

  def findBy(userId: UserId): F[List[Order]]

  def create(
      userId: UserId,
      paymentId: PaymentId,
      items: NonEmptyList[CartItem],
      total: Money
  ): F[OrderId]
}
