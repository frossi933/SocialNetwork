package com.social.network.services

import cats.data.Kleisli
import com.social.network.model.User
import com.social.network.requests.{LoginRequest, SignupRequest}
import com.social.network.validations.Validator.ValidationResult
import org.http4s.Request

trait AuthenticationService[F[_]] {

  type Token = String

  def signup(request: SignupRequest): F[ValidationResult[User]]
  def login(request: LoginRequest): F[ValidationResult[Token]]

  def authenticator: Kleisli[F, Request[F], Either[String, User]]

}
