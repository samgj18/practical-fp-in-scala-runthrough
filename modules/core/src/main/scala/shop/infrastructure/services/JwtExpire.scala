package shop.infrastructure.services

import shop.algebras.JwtExpire
import shop.infrastructure.config.types.TokenExpiration
import shop.infrastructure.effects._

import cats.effect.Sync
import cats.syntax.all._
import pdi.jwt.JwtClaim

object JwtExpire {
  def make[F[_]: Sync]: F[JwtExpire[F]] =
    JwtClock[F].utc.map { implicit jClock =>
      new JwtExpire[F] {
        def expiresIn(claim: JwtClaim, exp: TokenExpiration): F[JwtClaim] =
          Sync[F].delay(claim.issuedNow.expiresIn(exp.value.toMillis))
      }
    }
}
