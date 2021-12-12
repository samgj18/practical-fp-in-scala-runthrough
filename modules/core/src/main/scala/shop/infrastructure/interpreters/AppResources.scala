package shop.infrastructure.interpreters

import shop.infrastructure.config.types._

import cats.effect.std.Console
import cats.effect.{ Concurrent, Resource }
import cats.syntax.all._
import dev.profunktor.redis4cats.RedisCommands
import eu.timepit.refined.auto._
import fs2.io.net.Network
import natchez.Trace.Implicits.noop
import org.http4s.client.Client
import org.typelevel.log4cats.Logger
import skunk._
import skunk.codec.text._
import skunk.implicits._

// With sealed class we don't have the copy method for the class
// With the sealed abstract class we need to add the val keyword to make value accessible from the outside

sealed abstract class AppResources[F[_]](
    val client: Client[F],
    val postgres: Resource[F, Session[F]],
    val redis: RedisCommands[F, String, String]
)

object AppResources {
  def make[F[_]: Concurrent: Logger: Console: Network](
      postgres: Resource[F, Session[F]]
  ): Resource[F, AppResources[F]] = {

    def checkPostgresConnection(
        postgres: Resource[F, Session[F]]
    ): F[Unit] = postgres.use { session =>
      session.unique(sql"select version();".query(text)).flatMap { v =>
        Logger[F].info(s"Postgres version: $v")
      }
    }

    def checkRedisConnection(
        redis: RedisCommands[F, String, String]
    ): F[Unit] = redis.info.flatMap {
      _.get("redis_version").traverse_ { v =>
        Logger[F].info(s"Connected to Redis $v")
      }
    }

    def mkPostgreSqlResource(c: PostgreSQLConfig): SessionPool[F] =
      Session
        .pooled[F](
          host = c.host,
          port = c.port,
          user = c.user,
          password = Some(c.password.value),
          database = c.database,
          max = c.max.value
        )
        .evalTap(checkPostgresConnection)

    ???
  }
}
