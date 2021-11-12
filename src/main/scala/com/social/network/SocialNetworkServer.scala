package com.social.network

import cats.effect.{ConcurrentEffect, ContextShift, ExitCode, Timer}
import cats.implicits._
import com.social.network.config.DBConfig
import com.social.network.repositories.{PostsRepositoryDB, UsersRepositoryDB}
import fs2.Stream
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext.global

object SocialNetworkServer {

  def stream[F[_]: ConcurrentEffect: ContextShift](implicit T: Timer[F]): Stream[F, ExitCode] = {

      val services = SocialNetworkRoutes.postsRoutes[F](PostsRepositoryDB(DBConfig())) <+>
        SocialNetworkRoutes.userRoutes[F](UsersRepositoryDB(DBConfig()))

      val httpApp = Router("/" -> services).orNotFound

      // With Middlewares in place
      val finalHttpApp = Logger.httpApp(true, true)(httpApp)

      BlazeServerBuilder[F](global)
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .serve
  }
}
