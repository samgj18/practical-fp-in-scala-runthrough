package shop.infrastructure.effects

import java.util.UUID

import cats.ApplicativeThrow
import cats.effect.Sync

trait GenUUID[F[_]] {
  def genUUID: F[UUID]
  def read(str: String): F[UUID]
}

object GenUUID {
  def apply[F[_]: GenUUID]: GenUUID[F] = implicitly

  implicit def forSync[F[_]: Sync]: GenUUID[F] =
    new GenUUID[F] {
      def genUUID: F[UUID] = Sync[F].delay(UUID.randomUUID())

      def read(str: String): F[UUID] =
        ApplicativeThrow[F].catchNonFatal(UUID.fromString(str))
    }
}
