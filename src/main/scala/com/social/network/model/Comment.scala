package com.social.network.model

import cats.effect.Sync
import com.social.network.model.Post.PostId
import com.social.network.model.User.UserId
import io.circe.{Decoder, Encoder, KeyDecoder, KeyEncoder}
import io.circe.generic.semiauto._
import io.estatico.newtype.Coercible
import io.estatico.newtype.ops.toCoercibleIdOps
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

import java.time.Instant

case class Comment(id: CommentId, postId: PostId, authorId: UserId, text: CommentText, timestamp: Instant)

object Comment {

  implicit def coercibleDecoder[
    A: Coercible[B, *],
    B: Decoder
  ]: Decoder[A] = Decoder[B].map(_.coerce[A])
  implicit def coercibleEncoder[
    A: Coercible[*, B],
    B: Encoder
  ]: Encoder[A] = Encoder[B].contramap(_.coerce[B])
  implicit def coercibleKeyDecoder[A: Coercible[B, *], B: KeyDecoder]
  : KeyDecoder[A] =
    KeyDecoder[B].map(_.coerce[A])

  implicit def coercibleKeyEncoder[A: Coercible[*, B], B: KeyEncoder]
  : KeyEncoder[A] =
    KeyEncoder[B].contramap[A](_.coerce[B])

  implicit val commentDecoder: Decoder[Comment] = deriveDecoder[Comment]
  implicit def commentEntityDecoder[F[_]: Sync]: EntityDecoder[F, Comment] =
    jsonOf
  implicit val commentEncoder: Encoder[Comment] = deriveEncoder[Comment]
  implicit def commentEntityEncoder[F[_]]: EntityEncoder[F, Comment] =
    jsonEncoderOf
}