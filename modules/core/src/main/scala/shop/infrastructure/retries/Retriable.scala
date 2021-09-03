package shop.retries

import derevo.cats.show
import derevo.derive

/**
  * A type class to provide textual representation. It is meant to be a
  * better "toString". Whereas toString exists for any Object,
  * regardless of whether or not the creator of the class explicitly
  * made a toString method, a Show instance will only exist if someone
  * explicitly provided one.
  */
@derive(show)
sealed trait Retriable

object Retriable {
  case object Orders   extends Retriable
  case object Payments extends Retriable
}
