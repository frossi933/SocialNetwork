package com.social.network

import cats.effect.{ConcurrentEffect, ContextShift, ExitCode, Timer}
import cats.implicits._
import com.social.network.config.DBConfig
import com.social.network.model.User
import com.social.network.repositories.{CommentsRepositoryDB, PostsRepositoryCompressionWrapper, PostsRepositoryDB, UsersRepositoryDB}
import com.social.network.routes.{AuthRoutes, PostsRoutes, ProfileRoutes}
import com.social.network.services.{AuthenticationServiceCookies, PostsServiceImpl, ProfileServiceImpl}
import fs2.Stream
import org.http4s.implicits._
import org.http4s.server.{AuthMiddleware, Router}
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext.global

object SocialNetworkServer {

  def stream[F[_]: ConcurrentEffect: ContextShift](implicit T: Timer[F]): Stream[F, ExitCode] = {

    val dbConfig = DBConfig()
    val usersRepo = UsersRepositoryDB(dbConfig)
    val postsRepo = PostsRepositoryCompressionWrapper(PostsRepositoryDB(dbConfig))
    val commentsRepo = CommentsRepositoryDB(dbConfig)
    val authService = AuthenticationServiceCookies[F](usersRepo)
    val profileService = ProfileServiceImpl(postsRepo, commentsRepo)
    val postsService = PostsServiceImpl(postsRepo, commentsRepo)

    val middleware: AuthMiddleware[F, User] =
      AuthMiddleware(authService.authenticator, AuthRoutes.onFailure)

    val services =
      AuthRoutes.routes[F](authService) <+>
      middleware(PostsRoutes.authedRoutes[F](postsService)) <+>
      middleware(ProfileRoutes.authedRoutes[F](profileService, postsService))

    val httpApp = Router("/" -> services).orNotFound

    val finalHttpApp = Logger.httpApp(true, true)(httpApp)

    BlazeServerBuilder[F](global)
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(finalHttpApp)
      .serve
  }
}
