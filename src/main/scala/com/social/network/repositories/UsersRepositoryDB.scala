package com.social.network.repositories

import cats.effect.Bracket
import com.social.network.config.DBConfig
import com.social.network.model.{User, UserId}
import doobie.implicits._

trait UsersRepositoryDB[F[_]] extends UsersRepository[F]

object UsersRepositoryDB {

  def apply[F[_]](config: DBConfig[F])(implicit ev: Bracket[F, Throwable]): UsersRepositoryDB[F] = new UsersRepositoryDB[F] {

    override def createUser(email: String, name: String, password: String): F[User] =
      sql"""
        insert into users(email, name, password, active)
        values($email, $name, $password, true)
      """.update
        .withUniqueGeneratedKeys[User]("id", "email", "name", "password", "active")
        .transact(config.xa)

    override def getUserById(userId: UserId): F[Option[User]] =
      sql"""
        select id, email, name, password, active
        from users
         where id = $userId
      """.query[User]
        .option
        .transact(config.xa)

    override def getUserByEmail(email: String): F[Option[User]] =
      sql"""
        select id, email, name, password, active
        from users
        where email = $email
      """.query[User]
        .option
        .transact(config.xa)

    override def saveUser(user: User): F[User] =
      sql"""
        update users
        set email = ${user.email}, name = ${user.name}, password = ${user.password}
        where id = ${user.id}
      """.update
        .withUniqueGeneratedKeys[User]("id", "email", "name", "password", "active")
        .transact(config.xa)

  }
}
