package com.social.network.repositories

import com.social.network.model.User
import com.social.network.model.User.UserId


trait UsersRepository[F[_]] {

  def createUser(email: String, name: String): F[Option[User]]
  def getUserById(userId: UserId): F[Option[User]]

}
