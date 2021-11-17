package com.social.network.repositories

import com.social.network.model.Comment
import com.social.network.model.Comment.CommentId
import com.social.network.model.Post.PostId
import com.social.network.model.User.UserId

trait CommentsRepository[F[_]] {

  def createComment(postId: PostId, authorId: UserId, text: String): F[Comment]
  def getCommentById(commentId: CommentId): F[Option[Comment]]
  def getCommentsByPostId(postId: PostId): F[List[Comment]]
  def updateComment(comment: Comment): F[Comment]

}
