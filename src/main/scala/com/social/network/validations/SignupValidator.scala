package com.social.network.validations

import cats.implicits.catsSyntaxTuple3Semigroupal
import com.social.network.requests.SignupRequest
import com.social.network.validations.Validator.ValidationResult

object SignupValidator {

  def validateEmail(email: String): ValidationResult[String] =
    Validator.cond(
      email.matches("^\\S+@\\S+\\.\\S+$"),
      email,
      EmailMalformed
    )

  def validateName(name: String): ValidationResult[String] =
    Validator.cond(
      name.matches("^[a-zA-Z\\s]+$"),
      name,
      NameHasSpecialCharacters
    )

  def validatePassword(password: String): ValidationResult[String] =
    Validator.cond(
      password.matches("(?=^.{10,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$"),
      password,
      PasswordDoesNotMeetCriteria
    )

  def validateSignupRequest(signupRequest: SignupRequest): ValidationResult[SignupRequest] = {
    (validateEmail(signupRequest.email),
    validateName(signupRequest.name),
    validatePassword(signupRequest.password))
      .mapN(SignupRequest.apply)
  }
}
