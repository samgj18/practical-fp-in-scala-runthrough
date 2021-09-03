package shop.domains

import java.util.UUID

import io.estatico.newtype.macros.newtype

object auth {
  @newtype case class UserId(value: UUID)
  @newtype case class JwtToken(value: String)
  @newtype case class UserName(value: String)
  @newtype case class Password(value: String)
  @newtype case class EncrytedPassword(value: String)

  case class UserWithPassword(
      id: UserId,
      name: UserName,
      password: EncrytedPassword
  )

  case class User(id: UserId, name: UserName)
}
