package shop.domains

import java.util.UUID

import derevo.circe.magnolia.{ decoder, encoder }
import derevo.derive
import io.estatico.newtype.macros.newtype

object category {
  @derive(decoder, encoder)
  case class CategoryId(value: UUID)

  @derive(decoder, encoder)
  @newtype case class CategoryName(value: String)

  @derive(decoder, encoder)
  case class Category(uuid: CategoryId, name: CategoryName)
}
