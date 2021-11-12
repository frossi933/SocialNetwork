package com.social.network.repositories

import com.social.network.model.Post


trait PostsRepository[F[_]] {

  def createPost(authorId: Int, maybeText: Option[String], maybeImage: Option[String]): F[Post]
  def getPostById(id: Int): F[Option[Post]]
  def getPostsByUser(userId: Int): F[List[Post]]
  def getAll(): F[List[Post]]

}
