package shop.infrastructure.http.routes

import shop.algebras.Brands

import cats.Monad
import org.http4s._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

/**
  * We have a new Monad[F] constraint, required by HttpRoutes.of[F] but also needed to create a response.
  *
  */
final case class BrandRoutes[F[_]: Monad](
    brands: Brands[F]
) extends Http4sDsl[F] {
  private[routes] val prefixPath = "/brands"

  /**
    * Ok.apply builds a response with code 200 (Ok).
    * To build the response body, Http4s requires an EntityEncoder[F, A], where A is
        the return type of brands.findAll, in this case, List[Brand]. Well, technically it is
        F[List[Brand]], but the library will flatMap that for us and return a Response[F].
    * When using the JSON encoding via Circe, it is enough to add import org.http4s.circe.CirceEntityEncoder._ in scope.
    * To use JSON encoding via derevo an instance of @derive(encoder) macro needs to be atop of the entities
    */
  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root =>
      Ok(brands.findAll)
  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )
}
