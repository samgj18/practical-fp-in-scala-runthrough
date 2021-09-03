package shop.domains

import java.util.UUID

import io.estatico.newtype.macros.newtype
import derevo.derive
import derevo.circe.magnolia.decoder
import derevo.circe.magnolia.encoder

object auth {

  @derive(decoder, encoder)
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

  @newtype case class CommonUser(value: User)
}
