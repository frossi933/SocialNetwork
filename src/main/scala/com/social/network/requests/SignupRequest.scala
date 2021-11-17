package com.social.network.requests

import cats.effect.Sync
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.circe.jsonOf
import org.http4s.EntityDecoder

case class SignupRequest(email: String, name: String, password: String)

object SignupRequest {

  implicit val signupRequestDecoder: Decoder[SignupRequest] = deriveDecoder
  implicit def signupRequestEntityDecoder[F[_]: Sync]: EntityDecoder[F, SignupRequest] =
    jsonOf

}
