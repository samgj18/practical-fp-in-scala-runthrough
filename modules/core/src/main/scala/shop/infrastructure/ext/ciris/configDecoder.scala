package shop.infrastructure.ext.ciris

import shop.infrastructure.ext.derevo.Derive

import ciris.ConfigDecoder

object configDecoder extends Derive[Decoder.Id]

object Decoder {
  type Id[A] = ConfigDecoder[String, A]
}
