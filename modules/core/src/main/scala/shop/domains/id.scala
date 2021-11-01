package shop.domains

import shop.infrastructure.effects.GenUUID
import shop.infrastructure.optics.IsUUID

import cats.Functor
import cats.syntax.functor._

object ID {
  def make[F[_]: Functor: GenUUID, A: IsUUID]: F[A] =
    GenUUID[F].genUUID.map(IsUUID[A]._UUID.get)

  def read[F[_]: Functor: GenUUID, A: IsUUID](str: String): F[A] =
    GenUUID[F].read(str).map(IsUUID[A]._UUID.get)
}
