package com.social.network.repositories

import cats.effect.Bracket
import com.social.network.config.DBConfig
import com.social.network.model.User
import doobie.implicits._

trait UsersRepositoryDB[F[_]] extends UsersRepository[F]

object UsersRepositoryDB {

  def apply[F[_]](config: DBConfig[F])(implicit ev: Bracket[F, Throwable]) = new UsersRepositoryDB[F] {
    override def createUser(email: String, name: String): F[Option[User]] = ???

    override def getUserById(userId: User.UserId): F[Option[User]] =
      sql"select id, email, name, active from users where id = $userId".query[User].option.transact(config.xa)
  }
}
