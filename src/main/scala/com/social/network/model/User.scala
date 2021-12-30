package com.social.network.model

import cats.effect.Sync
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}


case class User(id: UserId, email: String, name: String, password: String, active: Boolean)

object User {

  implicit val userDecoder: Decoder[User] = deriveDecoder
  implicit def userEntityDecoder[F[_]: Sync]: EntityDecoder[F, User] =
    jsonOf
  implicit val userEncoder: Encoder[User] = deriveEncoder
  implicit def userEntityEncoder[F[_]]: EntityEncoder[F, User] =
    jsonEncoderOf

}


