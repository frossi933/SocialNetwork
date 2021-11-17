package com.social.network.requests

import cats.effect.Sync
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

case class LoginRequest(email: String, password: String)

object LoginRequest {

  implicit val loginRequestDecoder: Decoder[LoginRequest] = deriveDecoder
  implicit def loginRequestEntityDecoder[F[_]: Sync]: EntityDecoder[F, LoginRequest] =
    jsonOf

}
