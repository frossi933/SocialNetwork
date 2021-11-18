package com.social.network.services

import cats.implicits._
import cats.effect.Sync
import com.social.network.model.{Post, User}
import com.social.network.repositories.PostsRepository
import com.social.network.utils.Sorting
import com.social.network.validations.Validator.ValidationResult

trait ProfileServiceImpl[F[_]] extends ProfileService[F]

object ProfileServiceImpl {

  def apply[F[_]: Sync](postsRepo: PostsRepository[F]): ProfileService[F] = new ProfileServiceImpl[F] {

    override def getProfilePosts(user: User, sorting: Sorting): F[ValidationResult[List[Post]]] =
      postsRepo.getPostsByAuthorId(user.id, sorting).map(_.validNec)

  }
}
