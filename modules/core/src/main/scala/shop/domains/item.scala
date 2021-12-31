package shop.domains

import java.util.UUID

import shop.domains.brand.{ Brand, BrandId }
import shop.domains.cart._
import shop.domains.category.{ Category, CategoryId }
import shop.infrastructure.optics.uuid

import derevo.cats._
import derevo.circe.magnolia._
import derevo.derive
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.cats._
import eu.timepit.refined.string.{ Uuid, ValidBigDecimal }
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.refined._
import io.estatico.newtype.macros.newtype
import squants.market._

object item {

  /**
    * keyEncoder ->
    * A type class that provides a conversion from a value of type `A` to a string.
    *
    * This type class will be used to create strings for JSON keys when encoding
    * `Map[A, ?]` instances as JSON.
    *
    * Note that if more than one value maps to the same string, the resulting JSON
    * object may have fewer fields than the original map.
    *
    * keyDecoder ->
    * A type class that provides a conversion from a string used as a JSON key to a
    * value of type `A`.
    */
  @derive(decoder, encoder, keyEncoder, keyDecoder, show, uuid)
  @newtype case class ItemId(value: UUID)
  @derive(decoder, encoder, eqv, show)
  @newtype case class ItemName(value: String)
  @derive(decoder, encoder, eqv, show)
  @newtype case class ItemDescription(value: String)

  @derive(decoder, encoder, show)
  @newtype case class ItemNameParam(value: NonEmptyString)
  @derive(decoder, encoder, show)
  @newtype case class ItemDescriptionParam(value: NonEmptyString)
  @derive(decoder, encoder, show)
  @newtype case class PriceParam(value: String Refined ValidBigDecimal)
  @derive(decoder, encoder)
  @newtype case class ItemIdParam(value: String Refined Uuid)

  /**
    * This decoding and encoding depend on the package file on domains which
    * teach the compiler how to interpret the macros via moneyDecoder and moneyEncoder
    */
  @derive(decoder, encoder, show)
  case class Item(
      uuid: ItemId,
      name: ItemName,
      description: ItemDescription,
      price: Money,
      brand: Brand,
      category: Category
  ) {
    def cart(q: Quantity): CartItem =
      CartItem(this, q)
  }

  @derive(decoder, encoder, show)
  case class CreateItemParam(
      name: ItemNameParam,
      description: ItemDescriptionParam,
      price: PriceParam,
      brandId: BrandId,
      categoryId: CategoryId
  ) {
    def toDomain: CreateItem =
      CreateItem(
        ItemName(name.value),
        ItemDescription(description.value),
        USD(BigDecimal(price.value)),
        brandId,
        categoryId
      )
  }

  @derive(decoder, encoder)
  case class UpdateItemParam(
      id: ItemIdParam,
      price: PriceParam
  ) {
    def toDomain: UpdateItem =
      UpdateItem(
        ItemId(UUID.fromString(id.value)),
        USD(BigDecimal(price.value))
      )
  }

  case class CreateItem(
      name: ItemName,
      description: ItemDescription,
      price: Money,
      brandId: BrandId,
      categoryId: CategoryId
  )

  case class UpdateItem(
      id: ItemId,
      price: Money
  )
}
