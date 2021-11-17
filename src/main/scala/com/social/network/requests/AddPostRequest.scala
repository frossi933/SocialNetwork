package com.social.network.requests

import cats.effect.Sync
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.circe.jsonOf
import org.http4s.EntityDecoder

case class AddPostRequest(text: Option[String], image: Array[Byte])

object AddPostRequest {

  implicit val addPostRequestDecoder: Decoder[AddPostRequest] = deriveDecoder
  implicit def addPostRequestEntityDecoder[F[_]: Sync]: EntityDecoder[F, AddPostRequest] =
    jsonOf

}
