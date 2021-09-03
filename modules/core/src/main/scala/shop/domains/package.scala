package shop

import io.circe.{ Decoder, Encoder }
import squants.market.{ Money, USD }

package object domains extends OrphanInstances

trait OrphanInstances {
  implicit val moneyDecoder: Decoder[Money] =
    Decoder[BigDecimal].map(USD.apply)

  implicit val moneyEncoder: Encoder[Money] =
    Encoder[BigDecimal].contramap(_.amount)
}
