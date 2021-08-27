package shop.algebras

import shop.domains.auth.UserId
import shop.domains.cart.{ Cart, CartTotal, Quantity }
import shop.domains.item.ItemId

trait ShoppingCart[F[_]] {
  def add(userId: UserId, itemId: ItemId, quantity: Quantity): F[Unit]
  def get(userId: UserId): F[CartTotal]
  def delete(userId: UserId): F[Unit]
  def removeItem(userId: UserId, itemId: ItemId): F[Unit]
  def update(userId: UserId, cart: Cart): F[Unit]
}
