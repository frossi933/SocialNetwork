package com.social.network.model

import cats.effect.Sync
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

import java.time.Instant


case class Post(id: PostId, authorId: UserId, text: Option[String], image: Array[Byte], timestamp: Instant)

object Post {

  implicit lazy val postDecoder: Decoder[Post] = deriveDecoder
  implicit def postEntityDecoder[F[_]: Sync]: EntityDecoder[F, Post] =
    jsonOf
  implicit lazy val postEncoder: Encoder[Post] = deriveEncoder[Post]
  implicit def postEntityEncoder[F[_]]: EntityEncoder[F, Post] =
    jsonEncoderOf
}

