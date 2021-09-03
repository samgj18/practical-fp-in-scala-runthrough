package shop.algebras

import shop.domains.health.AppStatus

trait HealthCheck[F[_]] {
  def status: F[AppStatus]
}
