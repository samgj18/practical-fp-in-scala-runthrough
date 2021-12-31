package shop

import java.util.UUID

import shop.domains.auth._
import shop.domains.brand._
import shop.domains.cart._
import shop.domains.category._
import shop.domains.checkout._
import shop.domains.item._
import shop.domains.order._
import shop.domains.payment._
import shop.infrastructure.http.auth.user._

import eu.timepit.refined.api.Refined
import eu.timepit.refined.scalacheck.string._
import eu.timepit.refined.string.ValidBigDecimal
import eu.timepit.refined.types.string.NonEmptyString
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import squants.market._

object generators {
  // This alternative is extremely slow
  /* val nonEmptyStringGen: Gen[String] =
    Gen.alphaStr.suchThat(_.nonEmpty) */

  val nonEmptyStringGen: Gen[String] =
    Gen
      .chooseNum(21, 40)
      .flatMap { n =>
        Gen.buildableOfN[String, Char](n, Gen.alphaChar)
      }

  def nesGen[A](f: String => A): Gen[A] =
    nonEmptyStringGen.map(f)

  def idGen[A](f: UUID => A): Gen[A] =
    Gen.uuid.map(f)

  val brandIdGen: Gen[BrandId] =
    idGen(BrandId.apply)

  val brandNameGen: Gen[BrandName] =
    nesGen(BrandName.apply)

  val brandGen: Gen[Brand] =
    for {
      i <- brandIdGen
      n <- brandNameGen
    } yield Brand(i, n)

  val categoryIdGen: Gen[CategoryId] =
    idGen(CategoryId.apply)

  val categoryNameGen: Gen[CategoryName] =
    nesGen(CategoryName.apply)

  val categoryGen: Gen[Category] =
    for {
      i <- categoryIdGen
      n <- categoryNameGen
    } yield Category(i, n)

  val moneyGen: Gen[Money] =
    Gen.posNum[Long].map(n => USD(BigDecimal(n)))

  val itemIdGen: Gen[ItemId] =
    idGen(ItemId.apply)

  val itemNameGen =
    nesGen(ItemName.apply)

  val itemDescriptionGen =
    nesGen(ItemDescription.apply)

  val itemGen: Gen[Item] =
    for {
      i <- itemIdGen
      n <- itemNameGen
      d <- itemDescriptionGen
      p <- moneyGen
      b <- brandGen
      c <- categoryGen
    } yield Item(i, n, d, p, b, c)

  val quantityGen: Gen[Quantity] =
    Gen.posNum[Int].map(Quantity.apply)

  val cartItemGen: Gen[CartItem] =
    for {
      i <- itemGen
      q <- quantityGen
    } yield CartItem(i, q)

  val cartTotalGen: Gen[CartTotal] =
    for {
      i <- Gen.nonEmptyListOf(cartItemGen)
      t <- moneyGen
    } yield CartTotal(i, t)

  val itemMapGen: Gen[(ItemId, Quantity)] =
    for {
      i <- itemIdGen
      q <- quantityGen
    } yield i -> q

  val cartGen: Gen[Cart] =
    Gen.nonEmptyMap(itemMapGen).map(Cart.apply)

  val cardNameGen: Gen[CardName] =
    Gen
      .stringOf(
        Gen.oneOf(('a' to 'z') ++ ('A' to 'Z'))
      )
      .map { x =>
        CardName(Refined.unsafeApply(x))
      }

  val paymentIdGen: Gen[PaymentId] =
    idGen(PaymentId.apply)

  val orderIdGen: Gen[OrderId] =
    idGen(OrderId.apply)

  private def sized(size: Int): Gen[Long] = {
    def go(s: Int, acc: String): Gen[Long] =
      Gen.oneOf(1 to 9).flatMap { n =>
        if (s == size) acc.toLong
        else go(s + 1, acc + n.toString)
      }

    go(0, "")
  }

  val cardGen: Gen[Card] =
    for {
      n <- cardNameGen
      u <- sized(16).map(x => CardNumber(Refined.unsafeApply(x)))
      x <- sized(4).map(x => CardExpiration(Refined.unsafeApply(x.toString)))
      c <- sized(3).map(x => CardCVV(Refined.unsafeApply(x.toInt)))
    } yield Card(n, u, x, c)

  // http routes generators

  val userIdGen: Gen[UserId] = idGen(UserId.apply)

  val userNameGen: Gen[UserName] =
    nesGen(UserName.apply)

  val userGen: Gen[User] =
    for {
      i <- userIdGen
      n <- userNameGen
    } yield User(i, n)

  val commonUserGen: Gen[CommonUser] =
    userGen.map(CommonUser(_))

  val adminUserGen: Gen[AdminUser] =
    userGen.map(AdminUser(_))

  val paymentGen: Gen[Payment] =
    for {
      i <- userIdGen
      m <- moneyGen
      c <- cardGen
    } yield Payment(i, m, c)

  val brandParamGen: Gen[BrandParam] =
    arbitrary[NonEmptyString].map(BrandParam(_))

  val passwordGen: Gen[Password] =
    nesGen(Password.apply)

  val encryptedPasswordGen: Gen[EncryptedPassword] =
    nesGen(EncryptedPassword.apply)

  val createItemParamGen: Gen[CreateItemParam] =
    for {
      n <- arbitrary[NonEmptyString].map(ItemNameParam(_))
      d <- arbitrary[NonEmptyString].map(ItemDescriptionParam(_))
      p <- arbitrary[String Refined ValidBigDecimal].map(PriceParam(_))
      b <- brandIdGen
      c <- categoryIdGen
    } yield CreateItemParam(n, d, p, b, c)
}
