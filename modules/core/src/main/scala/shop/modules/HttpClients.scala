package shop.modules

import shop.algebras.PaymentClient
import shop.infrastructure.config.types.PaymentConfig

import cats.effect.MonadCancelThrow
import org.http4s.circe.JsonDecoder
import org.http4s.client.Client

object HttpClients {
  def make[F[_]: JsonDecoder: MonadCancelThrow](
      cfg: PaymentConfig,
      client: Client[F]
  ): HttpClients[F] =
    new HttpClients[F] {
      def payment: PaymentClient[F] = PaymentClient.make[F](cfg, client)
    }
}

sealed trait HttpClients[F[_]] {
  def payment: PaymentClient[F]
}
