package com.social.network.routes

import cats.implicits._
import cats.effect.Sync
import com.social.network.model.Comment.CommentId
import com.social.network.model.Post.PostId
import com.social.network.model.{Post, User}
import com.social.network.requests.{AddCommentRequest, AddPostRequest}
import com.social.network.services.PostsService
import org.http4s.AuthedRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.Http4sDsl
import com.social.network.validations.Validator.ValidationResultOps

object PostsRoutes {

  def authedRoutes[F[_]: Sync](postsService: PostsService[F]): AuthedRoutes[User, F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    AuthedRoutes.of {

      case GET -> Root / "posts" as _ =>
        postsService.getAllPosts().toResponse()

      case authedReq @ POST -> Root / "posts" as user =>
        (for {
          addPostRequest <- authedReq.req.as[AddPostRequest]
          postAdded <- postsService.addPost(addPostRequest, user)
        } yield postAdded).toResponse()

      case GET -> Root / "posts" / IntVar(postId) as _ =>
        postsService.getPost(PostId(postId)).toResponse()

      case authedReq @ PUT -> Root / "posts" / IntVar(postId) as user =>
        (for {
          postRequest <- authedReq.req.as[Post]
          updatedPost <- postsService.updatePost(postRequest, PostId(postId), user)
        } yield updatedPost).toResponse()

      case GET -> Root / "posts" / IntVar(postId) / "comments" as _ =>
        postsService.getPostComments(PostId(postId)).toResponse()

      case authedReq @ POST -> Root / "posts" / IntVar(postId) / "comments" as user =>
        (for {
          addCommentRequest <- authedReq.req.as[AddCommentRequest]
          commentAdded <- postsService.addComment(addCommentRequest, PostId(postId), user)
        } yield commentAdded).toResponse()

      case GET -> Root / "posts" / IntVar(postId) / "comments" / IntVar(commentId) as _ =>
        postsService.getPostComment(PostId(postId), CommentId(commentId)).toResponse()
    }
  }
}
