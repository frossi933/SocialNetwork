package com.social.network.validations

import com.social.network.requests.AddCommentRequest
import com.social.network.validations.Validator.ValidationResult

object CommentValidator {

  private val COMMENT_TEXT_MAX_LENGTH: Int = 255

  def validateTextNotEmpty(text:String): ValidationResult[String] =
    Validator.cond(
      text.nonEmpty,
      text,
      CommentTextIsEmpty
    )

  def validateTextMaxLength(text: String): ValidationResult[String] =
    Validator.cond(
      text.length <= COMMENT_TEXT_MAX_LENGTH,
      text,
      CommentTextLengthExceed(COMMENT_TEXT_MAX_LENGTH)
    )

  def validateAddCommentRequest(addCommentRequest: AddCommentRequest): ValidationResult[AddCommentRequest] =
    validateTextNotEmpty(addCommentRequest.text)
      .andThen(validateTextMaxLength)
      .map(AddCommentRequest.apply)

}
