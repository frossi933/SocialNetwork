package com.social.network.repositories

import com.social.network.model.Post
import com.social.network.model.Post.PostId
import com.social.network.model.User.UserId


trait PostsRepository[F[_]] {

  def createPost(authorId: UserId, maybeText: Option[String], image: Array[Byte]): F[Post]
  def getPostById(id: PostId): F[Option[Post]]
  def getPostsByAuthorId(authorId: UserId): F[List[Post]]
  def getAll(): F[List[Post]]
  def savePost(post: Post): F[Post]

}
