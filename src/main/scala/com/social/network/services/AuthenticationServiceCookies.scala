package com.social.network.services

import cats.data.Kleisli
import cats.effect.Sync
import cats.implicits._
import com.social.network.model.User
import com.social.network.repositories.UsersRepository
import com.social.network.requests.{LoginRequest, SignupRequest}
import com.social.network.validations.Validator.ValidationResult
import com.social.network.validations.{LoginValidator, SignupValidator, UserEmailAlreadyExist, Validator}
import org.http4s.Request
import org.http4s.headers.Cookie
import org.reactormonk.{CryptoBits, PrivateKey}

import java.time.Clock

trait AuthenticationServiceCookies[F[_]] extends AuthenticationService[F]

object AuthenticationServiceCookies {

  def apply[F[_]](usersRepo: UsersRepository[F])(implicit F: Sync[F]): AuthenticationServiceCookies[F] = new AuthenticationServiceCookies[F] {

    val key: PrivateKey = PrivateKey(scala.io.Codec.toUTF8(scala.util.Random.alphanumeric.take(20).mkString("")))
    val crypto: CryptoBits = CryptoBits(key)
    val clock: Clock = java.time.Clock.systemUTC

    override def signup(request: SignupRequest): F[ValidationResult[User]] =
      usersRepo.getUserByEmail(request.email).map { maybeUser =>
        Validator.validateIsNotDefined(maybeUser, UserEmailAlreadyExist).andThen { _ =>
          SignupValidator.validateSignupRequest(request)
        }
      }.flatMap(_.map(req => usersRepo.createUser(req.email, req.name, req.password)).sequence)

    override def login(request: LoginRequest): F[ValidationResult[Token]] =
      usersRepo.getUserByEmail(request.email).flatMap { user =>
        LoginValidator.validateLoginRequest(request, user)
          .map(generateToken)
          .sequence
      }

    private def generateToken(user: User): F[String] = F.delay(crypto.signToken(user.email, clock.millis.toString))

    override def authenticator: Kleisli[F, Request[F], Either[String,User]] = Kleisli({ request =>
      (for {
        header <- request.headers.get[Cookie].toRight("Cookie parsing error")
        cookie <- header.values.toList.find(_.name == "authcookie").toRight("Couldn't find the authcookie")
        token <- crypto.validateSignedToken(cookie.content).toRight("Cookie invalid")
        message <- Either.catchOnly[NumberFormatException](token).leftMap(_.toString)
      } yield message)
        .map(email => usersRepo.getUserByEmail(email))
        .sequence
        .map(_.flatMap(Either.fromOption(_, "User doesn't exist"))
              .flatMap(user => Either.cond(user.active, user, "User currently inactive"))
        )
    })

  }

}
