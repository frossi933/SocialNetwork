package com.social.network.validations

import cats.Monad
import cats.implicits._
import cats.data.{NonEmptyChain, ValidatedNec}
import org.http4s.{EntityEncoder, Response}
import org.http4s.dsl.Http4sDsl


object Validator {

  type ValidationResult[A] = ValidatedNec[ValidationError, A]

  def cond[A](condition: Boolean, valid: A, invalid: ValidationError): ValidationResult[A] =
    if(condition) valid.validNec else invalid.invalidNec

  def validateIsDefined[A](maybeA: Option[A], validationError: NotFoundValidationError): ValidationResult[A] =
    Validator.cond(
      maybeA.isDefined,
      maybeA.get,
      validationError
    )

  def validateIsNotDefined[A](maybeA: Option[A], validationError: BadRequestValidationError): ValidationResult[Option[A]] =
    Validator.cond(
      maybeA.isEmpty,
      maybeA,
      validationError
    )

  implicit class ValidationResultOps[F[_]: Monad, A](vr: F[ValidationResult[A]]) {
    val dsl = new Http4sDsl[F]{}
    import dsl._

    def toResponse()(implicit ev: EntityEncoder[F, A], ev2: EntityEncoder[F, NonEmptyChain[ValidationError]]): F[Response[F]] =
      vr.flatMap(_.fold(errors => errors.head match {
        case _: BadRequestValidationError => BadRequest(errors)
        case _: ForbiddenValidationError => Forbidden(errors)
        case _: NotFoundValidationError => NotFound(errors)
      }, Ok(_)))
  }

}
