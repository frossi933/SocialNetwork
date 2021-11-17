package com.social.network.model

import cats.effect.Sync
import com.social.network.model.User.UserId
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

case class UserDTO(id: UserId, email: String, name: String)

object UserDTO {

  def apply(user: User): UserDTO = UserDTO(user.id, user.email, user.name)

  import com.social.network.model.User.{userIdEncoder, userIdDecoder}

  implicit val userDTODecoder: Decoder[UserDTO] = deriveDecoder
  implicit def userDTOEntityDecoder[F[_]: Sync]: EntityDecoder[F, UserDTO] =
    jsonOf
  implicit val userDTOEncoder: Encoder[UserDTO] = deriveEncoder
  implicit def userDTOEntityEncoder[F[_]]: EntityEncoder[F, UserDTO] =
    jsonEncoderOf
}
