package jg.coursework.customheroesapp.domain.use_case

import jg.coursework.customheroesapp.util.EMAIL_ADDRESS_PATTERN
import jg.coursework.customheroesapp.domain.model.LoginInputValidationType

class ValidateLoginInputUseCase() {
    operator fun invoke(email: String, password: String): LoginInputValidationType {
        if(email.isEmpty() || password.isEmpty()) {
            return LoginInputValidationType.EmptyField
        }
        if(!EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
            return LoginInputValidationType.NoEmail
        }
        return LoginInputValidationType.Valid
    }
}

