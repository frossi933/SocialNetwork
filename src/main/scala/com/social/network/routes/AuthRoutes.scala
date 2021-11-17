package com.social.network.routes

import cats.Applicative
import cats.data.{Kleisli, OptionT}
import cats.effect.Sync
import cats.implicits._
import com.social.network.model.UserDTO
import com.social.network.requests.{LoginRequest, SignupRequest}
import com.social.network.services.AuthenticationService
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.{AuthedRoutes, HttpRoutes}
import org.http4s.dsl.Http4sDsl
import io.circe.Encoder._
import com.social.network.validations.Validator.ValidationResultOps

object AuthRoutes {

  def routes[F[_]: Sync](authenticationService: AuthenticationService[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case req @ POST -> Root / "signup" =>
        (for {
          signupRequest <- req.as[SignupRequest]
          userCreated <- authenticationService.signup(signupRequest)
          userCreatedDTO <- userCreated.map(UserDTO.apply).pure[F]
        } yield userCreatedDTO).toResponse()

      case req @ POST -> Root / "login" =>
        (for {
          loginRequest <- req.as[LoginRequest]
          user <- authenticationService.login(loginRequest)
        } yield user).toResponse()
    }
  }

  def onFailure[F[_]: Applicative]: AuthedRoutes[String, F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._

    Kleisli(req => OptionT.liftF(Forbidden(req.context)))
  }
}
