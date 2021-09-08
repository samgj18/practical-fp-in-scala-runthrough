package shop.infrastructure.http.routes.auth

import shop.algebras.Auth
import shop.domains.auth._

import cats.Monad
import cats.syntax.all._
import dev.profunktor.auth.AuthHeaders
import org.http4s.dsl.Http4sDsl
import org.http4s.server.{ AuthMiddleware, Router }
import org.http4s.{ AuthedRoutes, HttpRoutes }
final case class LogoutRoutes[F[_]: Monad](auth: Auth[F]) extends Http4sDsl[F] {
  private[routes] val prefixPath = "/auth"

  private val httpRoutes: AuthedRoutes[CommonUser, F] =
    AuthedRoutes.of {

      /**
        * traverse_ ->
        * This method is primarily useful when `G[_]` represents an action
        * or effect, and the specific `A` aspect of `G[A]` is not otherwise
        * needed.
        */
      case authRequest @ POST -> Root / "logout" as user =>
        AuthHeaders
          .getBearerToken(authRequest.req)
          .traverse_(auth.logout(_, user.value.name)) >> NoContent()
    }

  def routes(authMiddleware: AuthMiddleware[F, CommonUser]): HttpRoutes[F] = Router(
    prefixPath -> authMiddleware(httpRoutes)
  )
}
