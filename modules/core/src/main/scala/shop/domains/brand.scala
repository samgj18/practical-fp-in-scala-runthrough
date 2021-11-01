package shop.domains

import java.util.UUID

import shop.infrastructure.ext.http4s.queryParam
import shop.infrastructure.ext.http4s.refined._
import shop.infrastructure.optics.uuid

import derevo.cats._
import derevo.circe.magnolia.{ decoder, encoder }
import derevo.derive
import eu.timepit.refined.auto._
import eu.timepit.refined.cats._
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.refined._
import io.circe.{ Decoder, Encoder }
import io.estatico.newtype.macros.newtype

object brand {
  @derive(decoder, encoder, eqv, show, uuid)
  @newtype
  case class BrandId(value: UUID)

  @derive(decoder, encoder)
  case class BrandName(value: String) {
    def toBrand(brandId: BrandId): Brand =
      Brand(brandId, this)
  }

  @derive(decoder, encoder)
  case class Brand(uuid: BrandId, name: BrandName)

  @derive(queryParam, show)
  @newtype case class BrandParam(value: NonEmptyString) {
    def toDomain: BrandName = BrandName(value.toLowerCase.capitalize)
  }

  object BrandParam {
    implicit val jsonEncoder: Encoder[BrandParam] =
      Encoder.forProduct1("name")(_.value)

    implicit val jsonDecoder: Decoder[BrandParam] =
      Decoder.forProduct1("name")(BrandParam.apply)
  }

}
