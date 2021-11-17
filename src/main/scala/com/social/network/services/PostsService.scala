package com.social.network.services

import com.social.network.model.Comment.CommentId
import com.social.network.model.Post.PostId
import com.social.network.model.{Comment, Post, User}
import com.social.network.requests.{AddCommentRequest, AddPostRequest}
import com.social.network.validations.Validator.ValidationResult

trait PostsService[F[_]] {

  def getAllPosts(): F[ValidationResult[List[Post]]]
  def getPost(postId: PostId): F[ValidationResult[Post]]
  def addPost(addPostRequest: AddPostRequest, user: User): F[ValidationResult[Post]]
  def updatePost(postRequest: Post, postId: PostId, user: User): F[ValidationResult[Post]]
  def getPostComment(postId: PostId, commentId: CommentId): F[ValidationResult[Comment]]
  def getPostComments(postId: PostId): F[ValidationResult[List[Comment]]]
  def addComment(addCommentRequest: AddCommentRequest, postId: PostId, user: User): F[ValidationResult[Comment]]

}
