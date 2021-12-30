package com.social.network.repositories

import com.social.network.model.{Post, UserId, PostId}
import com.social.network.utils.Sorting


trait PostsRepository[F[_]] {

  def createPost(authorId: UserId, maybeText: Option[String], image: Array[Byte]): F[Post]
  def getPostById(id: PostId): F[Option[Post]]
  def getPostsByAuthorId(authorId: UserId, sorting: Sorting): F[List[Post]]
  def getAll(sorting: Sorting): F[List[Post]]
  def savePost(post: Post): F[Post]

}
