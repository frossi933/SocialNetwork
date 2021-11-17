package com.social.network.services

import com.social.network.model.Post.PostId
import com.social.network.model.{Comment, Post, User}
import com.social.network.validations.Validator.ValidationResult

trait ProfileService[F[_]] extends {

  def getProfilePosts(user: User): F[ValidationResult[List[Post]]]
  def getProfilePostComments(postId: PostId, user: User): F[ValidationResult[List[Comment]]]

}
