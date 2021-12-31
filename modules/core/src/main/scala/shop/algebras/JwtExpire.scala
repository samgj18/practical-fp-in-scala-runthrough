package shop.algebras

import shop.infrastructure.config.types.TokenExpiration

import pdi.jwt.JwtClaim

trait JwtExpire[F[_]] {
  def expiresIn(claim: JwtClaim, exp: TokenExpiration): F[JwtClaim]
}
