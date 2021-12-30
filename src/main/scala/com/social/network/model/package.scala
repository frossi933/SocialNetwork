package com.social.network

import doobie.util.meta.Meta
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.{Decoder, Encoder, KeyDecoder, KeyEncoder}
import io.estatico.newtype.ops.toCoercibleIdOps
import io.estatico.newtype.macros.newtype
import io.circe.refined._
import doobie.refined.implicits._
import io.estatico.newtype.Coercible

package object model {

  @newtype case class CommentId(id: Int)

  object CommentId {
    implicit val commentIdDecoder: Decoder[CommentId] = deriving
    implicit val commentIdEncoder: Encoder[CommentId] = deriving
    implicit val commentIdMeta: Meta[CommentId]       = deriving
  }

  @newtype case class CommentText(text: NonEmptyString)

  object CommentText {
    implicit val commentTextDecoder: Decoder[CommentText] = deriving
    implicit val commentTextEncoder: Encoder[CommentText] = deriving
    implicit val commentTextMeta: Meta[CommentText]       = deriving
  }

  @newtype case class PostId(id: Int)

  object PostId {
    implicit val postIdDecoder: Decoder[PostId] = deriving
    implicit val postIdEncoder: Encoder[PostId] = deriving
    implicit val postIdMeta: Meta[PostId]       = deriving
  }

  @newtype case class UserId(id: Int)

  object UserId {
    implicit val userIdDecoder: Decoder[UserId] = deriving
    implicit val userIdEncoder: Encoder[UserId] = deriving
    implicit val userIdMeta: Meta[UserId]       = deriving
  }

  object JsonCoercibleCodecs {
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
  }
}
