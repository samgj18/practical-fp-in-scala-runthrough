package shop.domains

import java.util.UUID

import io.estatico.newtype.macros.newtype

object category {
  case class CategoryId(value: UUID)

  @newtype
  case class CategoryName(value: String)

  case class Category(uuid: CategoryId, name: CategoryName)
}
