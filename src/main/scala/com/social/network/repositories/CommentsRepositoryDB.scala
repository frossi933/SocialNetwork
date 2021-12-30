package com.social.network.repositories

import cats.effect.Bracket
import com.social.network.config.DBConfig
import com.social.network.model.{Comment, CommentId, PostId, UserId}
import com.social.network.utils.Sorting
import doobie.Fragment
import doobie.implicits._
import doobie.postgres.implicits._


trait CommentsRepositoryDB[F[_]] extends CommentsRepository[F]

object CommentsRepositoryDB {

  def apply[F[_]](config: DBConfig[F])(implicit ev: Bracket[F, Throwable]): CommentsRepositoryDB[F] = new CommentsRepositoryDB[F] {

    override def createComment(postId: PostId, authorId: UserId, text: String): F[Comment] =
      sql"""
        insert into comments(post_id, author_id, text)
        values(${postId}, ${authorId}, ${text})
      """.update
        .withUniqueGeneratedKeys[Comment]("id", "post_id", "author_id", "text", "timestamp")
        .transact(config.xa)

    override def getCommentById(commentId: CommentId): F[Option[Comment]] =
      sql"""
        select id, post_id, author_id, text, timestamp
        from comments
        where id = $commentId
      """.query[Comment]
        .option
        .transact(config.xa)

    override def getCommentsByPostId(postId: PostId, sorting: Sorting): F[List[Comment]] =
      (fr"""
        select id, post_id, author_id, text, timestamp
        from comments
        where post_id = $postId
      """ ++ Fragment.const(Utils.sortingFragment("timestamp", "id", sorting)))
        .query[Comment]
        .to[List]
        .transact(config.xa)

    override def updateComment(comment: Comment): F[Comment] =
      sql"""
        update comments
        set text = ${comment.text}
        where id = ${comment.id}
      """.update
        .withUniqueGeneratedKeys[Comment]("id", "post_id", "author_id", "text", "timestamp")
        .transact(config.xa)
  }
}
