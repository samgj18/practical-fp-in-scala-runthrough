package shop.domains

import java.util.UUID

import io.estatico.newtype.macros.newtype

object auth {
  @newtype case class UserId(value: UUID)
}
