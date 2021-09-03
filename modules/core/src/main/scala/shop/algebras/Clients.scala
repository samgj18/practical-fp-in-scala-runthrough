package shop.algebras

import shop.domains.order.PaymentId
import shop.domains.payment.Payment

trait PaymentClient[F[_]] {
  def process(payment: Payment): F[PaymentId]
}
