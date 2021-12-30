package com.social.network.model

import cats.effect.Sync
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

import java.time.Instant

case class Comment(id: CommentId, postId: PostId, authorId: UserId, text: CommentText, timestamp: Instant)

object Comment {

  implicit val commentDecoder: Decoder[Comment] = deriveDecoder[Comment]
  implicit def commentEntityDecoder[F[_]: Sync]: EntityDecoder[F, Comment] =
    jsonOf
  implicit val commentEncoder: Encoder[Comment] = deriveEncoder[Comment]
  implicit def commentEntityEncoder[F[_]]: EntityEncoder[F, Comment] =
    jsonEncoderOf
}