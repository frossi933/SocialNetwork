package com.social.network.repositories

import cats.implicits._
import cats.MonadThrow
import com.social.network.model.{Post, PostId, UserId}
import com.social.network.utils.{ImageCompressor, Sorting}

import java.io.IOException

trait PostsRepositoryCompressionWrapper[F[_]] extends PostsRepository[F]

object PostsRepositoryCompressionWrapper {

  def apply[F[_]](base: PostsRepository[F])(implicit ev: MonadThrow[F]): PostsRepository[F] = new PostsRepositoryCompressionWrapper[F] {

    override def createPost(authorId: UserId, maybeText: Option[String], image: Array[Byte]): F[Post] =
      ImageCompressor.compress[F](image).flatMap { imageProcessed =>
        base.createPost(authorId, maybeText, imageProcessed)
      }

    override def getPostById(id: PostId): F[Option[Post]] =
      base.getPostById(id).flatMap(_.map(decompressPost).sequence)

    override def getPostsByAuthorId(authorId: UserId, sorting: Sorting): F[List[Post]] =
      base.getPostsByAuthorId(authorId, sorting).flatMap(decompressPosts)

    override def getAll(sorting: Sorting): F[List[Post]] =
      base.getAll(sorting).flatMap(decompressPosts)

    override def savePost(post: Post): F[Post] =
      compressPost(post).flatMap(base.savePost)

    private def compressPost(post: Post): F[Post] =
      ImageCompressor.compress[F](post.image).map(compressedImage => post.copy(image = compressedImage)).handleErrorWith {
        case _: IOException => post.pure[F]
      }

    private def decompressPost(post: Post): F[Post] =
      ImageCompressor.decompress[F](post.image).map(decompressedImage => post.copy(image = decompressedImage)).handleErrorWith {
        case _: IOException => post.pure[F]
      }

    private def decompressPosts(posts: List[Post]): F[List[Post]] =
      posts.map(decompressPost).sequence

  }
}
