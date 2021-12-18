package shop.algebras

import shop.domains.auth.{ EncryptedPassword, UserId, UserName }
import shop.infrastructure.http.auth.user.UserWithPassword

trait Users[F[_]] {
  def find(
      username: UserName
  ): F[Option[UserWithPassword]]

  def create(username: UserName, password: EncryptedPassword): F[UserId]

}
