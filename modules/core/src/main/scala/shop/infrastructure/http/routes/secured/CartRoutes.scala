package shop.infrastructure.http.routes.secured

import shop.algebras.ShoppingCart
import shop.domains.auth._
import shop.domains.cart._
import shop.infrastructure.http.vars._

import cats.Monad
import cats.syntax.all._
import org.http4s._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server._

final case class CartRoutes[F[_]: JsonDecoder: Monad](
    shoppingCart: ShoppingCart[F]
) extends Http4sDsl[F] {
  private[routes] val prefixPath = "/cart"

  private val httpRoutes: AuthedRoutes[CommonUser, F] =
    AuthedRoutes.of {
      // Get shopping cart
      case GET -> Root as user =>
        Ok(shoppingCart.get(user.value.id))

      // Add items to the cart
      case authRequest @ POST -> Root as user => {
        authRequest.req.asJsonDecode[Cart].flatMap {
          _.items
            .map {
              case (id, quantity) =>
                shoppingCart.add(user.value.id, id, quantity)
            }
            .toList
            .sequence >> Created()
        }
      }

      // Modify items in cart
      case authRequest @ PUT -> Root as user => {
        authRequest.req.asJsonDecode[Cart].flatMap { cart =>
          shoppingCart.update(user.value.id, cart) >> Ok()
        }
      }

      // Remove items from cart
      case DELETE -> Root / ItemIdVar(itemId) as user => {
        shoppingCart.removeItem(user.value.id, itemId) >> NoContent()
      }
    }

  def routes(authMiddleware: AuthMiddleware[F, CommonUser]): HttpRoutes[F] = Router(
    prefixPath -> authMiddleware(httpRoutes)
  )

}
