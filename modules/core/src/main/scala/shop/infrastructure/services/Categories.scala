package shop.infrastructure.services

import shop.algebras.Categories
import shop.domains.ID
import shop.domains.category._
import shop.infrastructure.effects.GenUUID
import shop.infrastructure.sql.codecs._

import cats.effect._
import cats.syntax.all._
import skunk._
import skunk.implicits._

object Categories {
  def make[F[_]: GenUUID: MonadCancelThrow](
      postgres: Resource[F, Session[F]]
  ): Categories[F] =
    new Categories[F] {
      import CategorySQL._

      def findAll: F[List[Category]] =
        postgres.use(_.execute(selectAll))

      def create(name: CategoryName): F[CategoryId] =
        postgres.use { session =>
          session.prepare(insertCategory).use { cmd =>
            ID.make[F, CategoryId].flatMap { id =>
              cmd.execute(Category(id, name)).as(id)
            }
          }
        }
    }
}

private[services] object CategorySQL {

  val codec: Codec[Category] =
    (categoryId ~ categoryName).imap {
      case i ~ n => Category(i, n)
    }(c => c.uuid ~ c.name)

  val selectAll: Query[Void, Category] =
    sql"""
        SELECT * FROM categories
       """.query(codec)

  val insertCategory: Command[Category] =
    sql"""
        INSERT INTO categories
        VALUES ($codec)
        """.command

}
