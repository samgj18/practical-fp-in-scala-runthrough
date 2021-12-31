package shop.algebras

import shop.domains.order._
import shop.domains.payment._
import shop.infrastructure.config.types

import cats.effect.MonadCancelThrow
import cats.syntax.all._
import eu.timepit.refined.auto._
import org.http4s.Method._
import org.http4s._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe._
import org.http4s.client._
import org.http4s.client.dsl.Http4sClientDsl

trait PaymentClient[F[_]] {
  def process(payment: Payment): F[PaymentId]
}

object PaymentClient {
  def make[F[_]: JsonDecoder: MonadCancelThrow](
      cfg: types.PaymentConfig,
      client: Client[F]
  ): PaymentClient[F] =
    new PaymentClient[F] with Http4sClientDsl[F] {
      def process(payment: Payment): F[PaymentId] =
        Uri.fromString(cfg.uri.value + "/payments").liftTo[F].flatMap { uri =>
          client.run(POST(payment, uri)).use { resp =>
            resp.status match {
              case Status.Ok | Status.Conflict =>
                resp.asJsonDecode[PaymentId]
              case st =>
                PaymentError(
                  Option(st.reason).getOrElse("unknown")
                ).raiseError[F, PaymentId]
            }
          }
        }
    }
}
