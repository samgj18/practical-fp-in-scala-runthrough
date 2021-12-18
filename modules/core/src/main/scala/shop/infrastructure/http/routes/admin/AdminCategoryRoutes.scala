package shop.infrastructure.http.routes.admin

import shop.algebras.Categories
import shop.domains.category._
import shop.infrastructure.ext.http4s.refined._
import shop.infrastructure.http.auth.user._

import cats.MonadThrow
import cats.syntax.all._
import io.circe.JsonObject
import io.circe.syntax._
import org.http4s._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server._

final case class AdminCategoryRoutes[F[_]: JsonDecoder: MonadThrow](
    categories: Categories[F]
) extends Http4sDsl[F] {
  private[admin] val prefixPath = "/categories"

  private val httpRoutes: AuthedRoutes[AdminUser, F] =
    AuthedRoutes.of {
      case authRequest @ POST -> Root as _ =>
        authRequest.req.decodeR[CategoryParam] { category =>
          categories.create(category.toDomain).flatMap { id =>
            Created(JsonObject.singleton("category_id", id.asJson))
          }
        }
    }

  def routes(authMiddleware: AuthMiddleware[F, AdminUser]): HttpRoutes[F] = Router(
    prefixPath -> authMiddleware(httpRoutes)
  )

}
