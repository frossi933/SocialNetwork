package com.social.network.repositories

import cats.effect.Bracket
import com.social.network.config.DBConfig
import com.social.network.model.{Post, PostId, UserId}
import com.social.network.utils.Sorting
import doobie.Fragment
import doobie.implicits._
import doobie.postgres.implicits._

trait PostsRepositoryDB[F[_]] extends PostsRepository[F]

object PostsRepositoryDB {

  def apply[F[_]](config: DBConfig[F])(implicit ev: Bracket[F, Throwable]) = new PostsRepository[F] {

    override def createPost(authorId: UserId, maybeText: Option[String], image: Array[Byte]): F[Post] =
      sql"""
        insert into posts(author_id, text, image)
        values ($authorId, $maybeText, $image)
      """.update
        .withUniqueGeneratedKeys[Post]("id", "author_id", "text", "image", "timestamp")
        .transact(config.xa)

    override def getPostById(id: PostId): F[Option[Post]] =
      sql"""
        select id, author_id, text, image, timestamp
        from posts
        where id = $id
      """.query[Post]
        .option
        .transact(config.xa)

    override def getPostsByAuthorId(authorId: UserId, sorting: Sorting): F[List[Post]] = {
      (fr"""
        select id, author_id, text, image, timestamp
        from posts
        where author_id = $authorId
      """ ++ Fragment.const(Utils.sortingFragment("timestamp", "id", sorting)))
        .query[Post]
        .to[List]
        .transact(config.xa)
    }

    override def getAll(sorting: Sorting): F[List[Post]] =
      (fr"""
        select id, author_id, text, image, timestamp
        from posts
      """ ++ Fragment.const(Utils.sortingFragment("timestamp", "id", sorting)))
        .query[Post]
        .to[List]
        .transact(config.xa)

    override def savePost(post: Post): F[Post] =
      sql"""
        update posts
        set text = ${post.text}, image = ${post.image}
        where id = ${post.id}
      """.update
        .withUniqueGeneratedKeys[Post]("id", "author_id", "text", "image", "timestamp")
        .transact(config.xa)
  }
}
