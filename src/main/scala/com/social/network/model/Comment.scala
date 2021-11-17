package com.social.network.model

import cats.effect.Sync
import com.social.network.model.Comment.CommentId
import com.social.network.model.Post.PostId
import com.social.network.model.User.UserId
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

import java.time.Instant


case class Comment(id: CommentId, postId: PostId, authorId: UserId, text: String, timestamp: Instant)

object Comment {

  final case class CommentId(id: Int) extends AnyVal

  import com.social.network.model.User.{userIdDecoder, userIdEncoder}
  import com.social.network.model.Post.{postIdDecoder, postIdEncoder}

  implicit val commentIdDecoder: Decoder[CommentId] = deriveDecoder
  implicit val commentDecoder: Decoder[Comment] = deriveDecoder
  implicit def commentEntityDecoder[F[_]: Sync]: EntityDecoder[F, Comment] =
    jsonOf
  implicit val commentIdEncoder: Encoder[CommentId] = deriveEncoder
  implicit val commentEncoder: Encoder[Comment] = deriveEncoder
  implicit def commentEntityEncoder[F[_]]: EntityEncoder[F, Comment] =
    jsonEncoderOf
}