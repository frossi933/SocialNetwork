package com.social.network

import doobie.util.meta.Meta
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.{Decoder, Encoder}
import io.estatico.newtype.macros.newtype
import io.circe.refined._
import doobie.refined.implicits._

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
}
