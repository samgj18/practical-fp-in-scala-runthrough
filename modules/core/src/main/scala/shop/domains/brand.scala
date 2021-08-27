package shop.domains

import java.util.UUID

object brand {
  case class BrandId(value: UUID)

  case class BrandName(value: String) {
    def toBrand(brandId: BrandId): Brand =
      Brand(brandId, this)
  }

  case class Brand(uuid: BrandId, name: BrandName)
}
