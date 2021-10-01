package shop.algebras

import shop.domains.order._
import shop.domains.payment._

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
      client: Client[F]
  ): PaymentClient[F] = new PaymentClient[F] with Http4sClientDsl[F] {

    val baseUri = "http: //localhost:8080/api/v1"

    def process(payment: Payment): F[PaymentId] = {
      Uri
        .fromString(baseUri + "/payments")
        .liftTo[F]
        .flatMap { uri =>
          client.fetchAs[PaymentId](POST(payment, uri))
        }
    }

  }
}
