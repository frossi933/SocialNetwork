package com.social.network.validations

import cats.implicits._
import com.social.network.model.{Post, User}
import com.social.network.requests.AddPostRequest
import com.social.network.validations.Validator.ValidationResult

object PostValidator {

  private val POST_TEXT_MAX_LENGTH: Int = 255
  private val POST_IMAGE_MAX_SIZE_MB: Int = 200

  def validatePostText(maybeText: Option[String]): ValidationResult[Option[String]] =
    maybeText.fold[ValidationResult[Option[String]]](maybeText.validNec)(text =>
      Validator.cond(
        text.length <= POST_TEXT_MAX_LENGTH,
        maybeText,
        PostTextLengthExceed(POST_TEXT_MAX_LENGTH)
      )
    )

  def validatePostImage(image: Array[Byte]): ValidationResult[Array[Byte]] =
    Validator.cond(
      !image.isEmpty,
      image,
      PostImageIsEmpty
    ).andThen(image =>
      Validator.cond(
        image.length <= POST_IMAGE_MAX_SIZE_MB * 1024 * 1024,
        image,
        PostImageIsTooBig(POST_IMAGE_MAX_SIZE_MB)
      )
    )

  def validateAddPostRequest(addPostRequest: AddPostRequest): ValidationResult[AddPostRequest] =
    (validatePostText(addPostRequest.text),
     validatePostImage(addPostRequest.image))
    .mapN(AddPostRequest.apply)

  def validatePostsMatchIdAndAuthor(postA: Post, postB: Post): ValidationResult[Post] =
    Validator.cond(
      postA.id == postB.id && postA.authorId == postB.authorId,
      postA,
      PostsDoNotMatch
    )

  def validatePostAuthor(post: Post, user: User): ValidationResult[Post] =
    Validator.cond(
      post.authorId == user.id,
      post,
      PostDoesNotBelongToLoggedUser
    )

}
