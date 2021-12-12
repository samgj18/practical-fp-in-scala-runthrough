package shop.domains

import java.util.UUID

import scala.util.control.NoStackTrace

import derevo.cats._
import derevo.circe.magnolia.{ decoder, encoder }
import derevo.derive
import eu.timepit.refined.auto._
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.refined._
import io.estatico.newtype.macros.newtype

object auth {

  @derive(decoder, encoder, show)
  @newtype case class UserId(value: UUID)
  @newtype case class JwtToken(value: String)
  @derive(decoder, encoder, show)
  @newtype case class UserName(value: String)
  @newtype case class Password(value: String)
  @newtype case class EncrytedPassword(value: String)

  case class UserWithPassword(
      id: UserId,
      name: UserName,
      password: EncrytedPassword
  )

  @derive(decoder, encoder)
  @newtype
  case class UserNameParam(value: NonEmptyString) {
    def toDomain: UserName = UserName(value.toLowerCase)
  }

  @derive(decoder, encoder)
  @newtype
  case class PasswordParam(value: NonEmptyString) {
    def toDomain: Password = Password(value)
  }

  @derive(decoder, encoder, eqv, show)
  @newtype
  case class EncryptedPassword(value: String)

  case class UserNotFound(username: UserName)    extends NoStackTrace
  case class UserNameInUse(username: UserName)   extends NoStackTrace
  case class InvalidPassword(username: UserName) extends NoStackTrace
  case object UnsupportedOperation               extends NoStackTrace

  @derive(show)
  case class User(id: UserId, name: UserName)

  @newtype case class CommonUser(value: User)
  @derive(show)
  @newtype
  case class AdminUser(value: User)
  @derive(decoder, encoder)
  case class CreateUser(
      username: UserNameParam,
      password: PasswordParam
  )

  @derive(decoder, encoder)
  case class LoginUser(
      username: UserNameParam,
      password: PasswordParam
  )

}
