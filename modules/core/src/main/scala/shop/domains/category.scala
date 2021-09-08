package shop.domains

import java.util.UUID

import derevo.circe.magnolia.{ decoder, encoder }
import derevo.derive
import eu.timepit.refined.auto._
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Decoder
import io.circe.refined._
import io.estatico.newtype.macros.newtype

object category {
  @derive(decoder, encoder)
  case class CategoryId(value: UUID)

  @derive(decoder, encoder)
  @newtype case class CategoryName(value: String)

  @derive(decoder, encoder)
  case class Category(uuid: CategoryId, name: CategoryName)

  @newtype
  case class CategoryParam(value: NonEmptyString) {
    def toDomain: CategoryName = CategoryName(value.toLowerCase.capitalize)
  }

  object CategoryParam {
    implicit val jsonDecoder: Decoder[CategoryParam] =
      Decoder.forProduct1("name")(CategoryParam.apply)
  }
}
