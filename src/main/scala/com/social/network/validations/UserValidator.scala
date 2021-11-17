package com.social.network.validations

import com.social.network.model.{User, UserDTO}
import com.social.network.validations.Validator.ValidationResult

object UserValidator {

  def validateUsersMatch(userToUpdate: UserDTO, loggedUser: User): ValidationResult[UserDTO] =
    Validator.cond(
      userToUpdate.id == loggedUser.id,
      userToUpdate,
      UsersDoNotMatch
    )

}
