package com.social.network.repositories

import cats.effect.Bracket
import com.social.network.config.DBConfig
import com.social.network.model.Post
import doobie.implicits._
import doobie.postgres.implicits._

trait PostsRepositoryDB[F[_]] extends PostsRepository[F]

object PostsRepositoryDB {

  def apply[F[_]](config: DBConfig[F])(implicit ev: Bracket[F, Throwable]) = new PostsRepository[F] {

    override def createPost(authorId: Int, maybeText: Option[String], maybeImage: Option[String]): F[Post] = {
      val timestamp = ""
      sql"""
        insert into posts (authorId, text, image, timestamp)
        values ($authorId, $maybeText, $maybeImage, $timestamp)
      """.update.withUniqueGeneratedKeys[Post]("id", "authorId", "text", "image", "timestamp").transact(config.xa)
    }

    override def getPostById(id: Int): F[Option[Post]] =
      sql"select id, authorId, text, image, timestamp from posts where id = $id"
        .query[Post]
        .option
        .transact(config.xa)(ev)

    override def getPostsByUser(userId: Int): F[List[Post]] =
      sql"select id, authorId, text, image, timestamp from posts where authorId = $userId"
        .query[Post]
        .to[List]
        .transact(config.xa)

    override def getAll(): F[List[Post]] =
      sql"select id, authorId, text, image, timestamp from posts"
        .query[Post]
        .to[List]
        .transact(config.xa)
  }
}
