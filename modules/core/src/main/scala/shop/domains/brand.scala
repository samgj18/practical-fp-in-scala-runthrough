package shop.domains

import java.util.UUID

import shop.infrastructure.ext.http4s.queryParam
import shop.infrastructure.ext.http4s.refined._

import derevo.circe.magnolia.{ decoder, encoder }
import derevo.derive
import eu.timepit.refined.auto._
import eu.timepit.refined.types.string.NonEmptyString
import io.estatico.newtype.macros.newtype

object brand {
  @derive(decoder, encoder)
  case class BrandId(value: UUID)

  @derive(decoder, encoder)
  case class BrandName(value: String) {
    def toBrand(brandId: BrandId): Brand =
      Brand(brandId, this)
  }

  @derive(decoder, encoder)
  case class Brand(uuid: BrandId, name: BrandName)

  @derive(queryParam)
  @newtype case class BrandParam(value: NonEmptyString) {
    def toDomain: BrandName = BrandName(value.toLowerCase.capitalize)
  }

}
