package shop.infrastructure.http.routes.secured

import shop.infrastructure.services.Checkout
import shop.infrastructure.ext.http4s.refined._
import shop.domains.auth._
import shop.domains.checkout._

import cats.MonadThrow
import cats.syntax.all._
import org.http4s._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server._
import shop.domains.order._
import shop.domains.cart._

final case class CheckoutRoutes[F[_]: JsonDecoder: MonadThrow](
    checkout: Checkout[F]
) extends Http4sDsl[F] {
  private[routes] val prefixPath = "/checkout"

  private val httpRoutes: AuthedRoutes[CommonUser, F] =
    AuthedRoutes.of {
      case authRequest @ POST -> Root as user =>
        authRequest.req.decodeR[Card] { card =>
          checkout
            .process(user.value.id, card)
            .flatMap(Created(_))
            .recoverWith {
              case CartNotFound(userId) =>
                NotFound(
                  s"Cart not found for user: ${userId.value}"
                )

              case EmptyCartError =>
                BadRequest("Shopping cart is empty!")

              case e: OrderOrPaymentError =>
                BadRequest(e.show)
            }
        }
    }

  def routes(authMiddleware: AuthMiddleware[F, CommonUser]): HttpRoutes[F] = Router(
    prefixPath -> authMiddleware(httpRoutes)
  )
}
