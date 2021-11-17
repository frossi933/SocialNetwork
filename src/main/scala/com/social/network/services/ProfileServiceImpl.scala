package com.social.network.services

import cats.implicits._
import cats.effect.Sync
import com.social.network.model.{Comment, Post, User}
import com.social.network.repositories.{CommentsRepository, PostsRepository}
import com.social.network.validations.{PostNotFound, PostValidator, Validator}
import com.social.network.validations.Validator.ValidationResult

trait ProfileServiceImpl[F[_]] extends ProfileService[F]

object ProfileServiceImpl {

  def apply[F[_]: Sync](postsRepo: PostsRepository[F], commentsRepo: CommentsRepository[F]): ProfileService[F] = new ProfileServiceImpl[F] {

    override def getProfilePosts(user: User): F[ValidationResult[List[Post]]] =
      postsRepo.getPostsByAuthorId(user.id).map(_.validNec)

    override def getProfilePostComments(postId: Post.PostId, user: User): F[ValidationResult[List[Comment]]] =
      postsRepo.getPostById(postId).map { maybePost =>
        Validator.validateIsDefined(maybePost, PostNotFound).andThen { post =>
          PostValidator.validatePostAuthor(post, user)
        }
      }.flatMap(_.map(post => commentsRepo.getCommentsByPostId(post.id)).sequence)
  }
}
