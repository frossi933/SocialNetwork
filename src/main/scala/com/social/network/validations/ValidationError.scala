package com.social.network.validations

import io.circe.Encoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

sealed trait ValidationError {
  def errorMessage: String
}

trait BadRequestValidationError extends ValidationError
trait NotFoundValidationError extends ValidationError
trait ForbiddenValidationError extends ValidationError

object ValidationError {

  implicit val validationErrorEncoder: Encoder[ValidationError] =
    Encoder.encodeString.contramap[ValidationError](_.errorMessage)
  implicit def validationErrorEntityEncoder[F[_]]: EntityEncoder[F, ValidationError] =
    jsonEncoderOf
}

case object EmailMalformed extends BadRequestValidationError {
  def errorMessage: String = "Invalid email."
}

case object PasswordDoesNotMeetCriteria extends BadRequestValidationError {
  def errorMessage: String = "Password must be at least 10 characters long, including an uppercase and a lowercase letter, one number and one special character."
}

case object NameHasSpecialCharacters extends BadRequestValidationError {
  def errorMessage: String = "Name cannot contain numbers or special characters."
}

case object UserEmailNotFound extends NotFoundValidationError {
  def errorMessage: String = "There is no user registered with that email account."
}

case object WrongPassword extends BadRequestValidationError {
  def errorMessage: String = "Wrong Password."
}

case object UsersDoNotMatch extends ForbiddenValidationError {
  def errorMessage: String = "Cannot perform the action because it affects an user other than the logged user."
}

case class PostTextLengthExceed(maxLength: Int) extends BadRequestValidationError {
  def errorMessage: String = s"Post text cannot exceed $maxLength characters."
}

case object PostImageIsEmpty extends BadRequestValidationError {
  def errorMessage: String = "Post image cannot be empty."
}

case class PostImageIsTooBig(max: Int) extends BadRequestValidationError {
  def errorMessage: String = s"Post image cannot exceed $max Mb."
}

case object PostDoesNotBelongToLoggedUser extends ForbiddenValidationError {
  def errorMessage: String = "Post does not belong to logged user."
}

case object PostsDoNotMatch extends ForbiddenValidationError {
  def errorMessage: String = "Post from body and post from url do not match."
}

case object CommentTextIsEmpty extends BadRequestValidationError {
  def errorMessage: String = "Text of comment cannot be empty."
}

case class CommentTextLengthExceed(maxLength: Int) extends BadRequestValidationError {
  def errorMessage: String = s"Comment text cannot exceed $maxLength characters."
}

case object UserEmailAlreadyExist extends BadRequestValidationError {
  def errorMessage: String = "Email already registered."
}

case object PostNotFound extends NotFoundValidationError {
  def errorMessage: String = "Post not found."
}

case object CommentNotFound extends NotFoundValidationError {
  def errorMessage: String = "Comment not found."
}