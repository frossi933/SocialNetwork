package com.social.network.services

import com.social.network.model.{Post, User}
import com.social.network.utils.Sorting
import com.social.network.validations.Validator.ValidationResult

trait ProfileService[F[_]] extends {

  def getProfilePosts(user: User, sorting: Sorting): F[ValidationResult[List[Post]]]

}
