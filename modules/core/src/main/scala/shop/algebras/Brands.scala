package shop.algebras

import shop.domains.brand.{ Brand, BrandId, BrandName }

trait Brands[F[_]] {
  def findAll: F[List[Brand]]
  def create(name: BrandName): F[BrandId]
}
