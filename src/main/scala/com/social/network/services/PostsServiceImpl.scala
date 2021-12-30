package com.social.network.services

import cats.implicits._
import cats.MonadThrow
import com.social.network.model.{Comment, CommentId, Post, PostId, User}
import com.social.network.repositories.{CommentsRepository, PostsRepository}
import com.social.network.requests.{AddCommentRequest, AddPostRequest}
import com.social.network.utils.Sorting
import com.social.network.validations.{CommentNotFound, CommentValidator, PostNotFound, PostValidator, ValidationError, Validator}
import com.social.network.validations.Validator.ValidationResult

trait PostsServiceImpl[F[_]] extends PostsService[F]

object PostsServiceImpl {

  def apply[F[_]: MonadThrow](postsRepo: PostsRepository[F], commentsRepo: CommentsRepository[F]): PostsService[F] = new PostsService[F] {

    override def getAllPosts(sorting: Sorting): F[ValidationResult[List[Post]]] =
      postsRepo.getAll(sorting).map(_.validNec[ValidationError])

    override def addPost(addPostRequest: AddPostRequest, user: User): F[ValidationResult[Post]] =
      PostValidator.validateAddPostRequest(addPostRequest).map { addPostRequest =>
        postsRepo.createPost(user.id, addPostRequest.text, addPostRequest.image)
      }.sequence

    override def getPost(postId: PostId): F[ValidationResult[Post]] =
      postsRepo.getPostById(postId).map { maybePost =>
        Validator.validateIsDefined(maybePost, PostNotFound)
      }

    override def updatePost(postRequest: Post, postId: PostId, user: User): F[ValidationResult[Post]] =
      postsRepo.getPostById(postId).map { maybePost =>
        Validator.validateIsDefined(maybePost, PostNotFound).andThen { post =>
          PostValidator.validatePostsMatch(postRequest, post).andThen { post =>
            PostValidator.validatePostAuthor(post, user)
          }
        }
      }.flatMap(_.map(postsRepo.savePost).sequence)

    override def getPostComments(postId: PostId, sorting: Sorting): F[ValidationResult[List[Comment]]] =
      postsRepo.getPostById(postId).map { maybePost =>
        Validator.validateIsDefined(maybePost, PostNotFound)
      }.flatMap(_.map(post => commentsRepo.getCommentsByPostId(post.id, sorting)).sequence)

    override def getPostComment(postId: PostId, commentId: CommentId): F[ValidationResult[Comment]] =
      for {
        postValidation <- postsRepo.getPostById(postId).map(Validator.validateIsDefined(_, PostNotFound))
        commentValidation <- commentsRepo.getCommentById(commentId).map(Validator.validateIsDefined(_, CommentNotFound))
      } yield postValidation.andThen(_ => commentValidation)

    override def addComment(addCommentRequest: AddCommentRequest, postId: PostId, user: User): F[ValidationResult[Comment]] =
      postsRepo.getPostById(postId).map { maybePost =>
        Validator.validateIsDefined(maybePost, PostNotFound).andThen { _ =>
          CommentValidator.validateAddCommentRequest(addCommentRequest)
        }
      }.flatMap(_.map(req => commentsRepo.createComment(postId, user.id, req.text)).sequence)
  }
}
