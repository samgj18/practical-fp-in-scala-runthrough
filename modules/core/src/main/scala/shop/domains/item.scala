package shop.domains

import java.util.UUID

import shop.domains.brand.{ Brand, BrandId }
import shop.domains.cart._
import shop.domains.category.{ Category, CategoryId }

import derevo.cats.show
import derevo.circe.magnolia._
import derevo.derive
import io.estatico.newtype.macros.newtype
import squants.Money

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
  @derive(decoder, encoder, keyEncoder, keyDecoder, show)
  @newtype case class ItemId(value: UUID)
  @derive(decoder, encoder)
  @newtype case class ItemName(value: String)
  @derive(decoder, encoder)
  @newtype case class ItemDescription(value: String)

  /**
    * This decoding and encoding depend on the package file on domains which
    * teach the compiler how to interpret the macros via moneyDecoder and moneyEncoder
    */
  @derive(decoder, encoder)
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
