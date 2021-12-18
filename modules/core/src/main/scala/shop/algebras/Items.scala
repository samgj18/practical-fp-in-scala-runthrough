package shop.algebras

import shop.domains.brand.BrandName
import shop.domains.item._

import fs2.Stream

trait Items[F[_]] {
  def findAll: Stream[F, Item]
  def findBy(brand: BrandName): Stream[F, Item]
  def findById(itemId: ItemId): F[Option[Item]]
  def create(item: CreateItem): F[ItemId]
  def update(item: UpdateItem): F[Unit]
}
