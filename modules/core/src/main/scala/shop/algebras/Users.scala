package shop.algebras

import shop.domains.auth.{ UserName, UserWithPassword }

trait Users[F[_]] {
  def find(
      username: UserName
  ): F[Option[UserWithPassword]]
}
