package com.social.network.validations

import com.social.network.model.User
import com.social.network.requests.LoginRequest
import com.social.network.validations.Validator.ValidationResult

object LoginValidator {

  def validatePassword(request: LoginRequest, user: User): ValidationResult[User] =
    Validator.cond(
      user.password == request.password,
      user,
      WrongPassword
    )

  def validateLoginRequest(request: LoginRequest, maybeUser: Option[User]): ValidationResult[User] =
    Validator.validateIsDefined(maybeUser, UserEmailNotFound)
      .andThen(validatePassword(request, _))

}
