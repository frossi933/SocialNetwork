package com.social.network

import cats.effect.Sync
import cats.implicits._
import com.social.network.model.User.UserId
import com.social.network.repositories.{PostsRepository, UsersRepository}
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.Http4sDsl


object SocialNetworkRoutes {

  def userRoutes[F[_]: Sync](usersRepo: UsersRepository[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "user" / IntVar(id) =>
        for {
          user <- usersRepo.getUserById(UserId(id))
          resp <- Ok(user.get) // FIXME
        } yield resp
    }
  }

  def postsRoutes[F[_]: Sync](postsRepo: PostsRepository[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "posts" =>
        for {
          posts <- postsRepo.getAll()
          resp <- Ok(posts)
        } yield resp
    }
  }
}