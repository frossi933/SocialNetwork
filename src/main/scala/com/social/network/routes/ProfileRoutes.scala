package com.social.network.routes

import cats.effect.Sync
import com.social.network.model.Post.PostId
import com.social.network.model.{User, UserDTO}
import com.social.network.services.{PostsService, ProfileService}
import org.http4s.AuthedRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.Http4sDsl
import com.social.network.validations.Validator._

object ProfileRoutes {

  def authedRoutes[F[_]: Sync](profileService: ProfileService[F], postsService: PostsService[F]): AuthedRoutes[User, F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    AuthedRoutes.of {

      case GET -> Root / "profile" as user =>
        Ok(UserDTO(user))

      case GET -> Root / "profile" / "posts" as user =>
        profileService.getProfilePosts(user).toResponse()

      case GET -> Root / "profile" / "posts" / IntVar(postId) / "comments" as _ =>
        postsService.getPostComments(PostId(postId)).toResponse()

    }
  }
}
