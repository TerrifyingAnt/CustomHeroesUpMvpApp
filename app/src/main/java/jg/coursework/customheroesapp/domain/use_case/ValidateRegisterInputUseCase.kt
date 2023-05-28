package jg.coursework.customheroesapp.domain.use_case

import jg.coursework.customheroesapp.util.EMAIL_ADDRESS_PATTERN
import jg.coursework.customheroesapp.domain.model.RegisterInputValidationType
import jg.coursework.customheroesapp.util.containsNumber
import jg.coursework.customheroesapp.util.containsSpecialChar
import jg.coursework.customheroesapp.util.containsUpperCase

class ValidateRegisterInputUseCase {
    operator fun invoke(
        email: String,
        password: String,
        passwordRepeated: String
    ): RegisterInputValidationType {
        if(email.isEmpty() || password.isEmpty() || passwordRepeated.isEmpty()) {
            return RegisterInputValidationType.EmptyField
        }
        if(!EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
            return RegisterInputValidationType.NoEmail
        }
        if(password != passwordRepeated) {
            return RegisterInputValidationType.PasswordsDoNotMatch
        }
        if(password.count() < 8) {
            return RegisterInputValidationType.PasswordTooShort
        }
        if(!password.containsNumber()) {
            return RegisterInputValidationType.PasswordNumberMissing
        }
        if(!password.containsUpperCase()) {
            return RegisterInputValidationType.PasswordUpperCaseMissing
        }
        if(!password.containsSpecialChar()) {
            return RegisterInputValidationType.PasswordSpecialCharMissing
        }
        return RegisterInputValidationType.Valid
    }
}