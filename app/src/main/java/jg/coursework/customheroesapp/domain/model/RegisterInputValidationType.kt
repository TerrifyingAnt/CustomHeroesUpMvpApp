package jg.coursework.customheroesapp.domain.model

enum class RegisterInputValidationType {
    EmptyField,
    NoEmail,
    PasswordsDoNotMatch,
    PasswordUpperCaseMissing,
    PasswordNumberMissing,
    PasswordSpecialCharMissing,
    PasswordTooShort,
    Valid
}