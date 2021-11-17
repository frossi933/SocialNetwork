package com.social.network.requests

import cats.effect.Sync
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.circe.jsonOf
import org.http4s.EntityDecoder

case class AddCommentRequest(text: String)

object AddCommentRequest {
  
  implicit val addCommentRequestDecoder: Decoder[AddCommentRequest] = deriveDecoder
  implicit def addCommentRequestEntityDecoder[F[_]: Sync]: EntityDecoder[F, AddCommentRequest] =
    jsonOf

}
