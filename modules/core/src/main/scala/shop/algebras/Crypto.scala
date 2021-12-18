package shop.algebras

import shop.domains.auth.{ EncryptedPassword, Password }

trait Crypto {
  def encrypt(value: Password): EncryptedPassword
  def decrypt(value: EncryptedPassword): Password
}
