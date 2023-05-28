package jg.coursework.customheroesapp.data.dto.RegisterDTO

data class RegisterRequestDTO(
    val name: String = "",
    val login: String,
    val password: String,
    val phoneNumber: String = "",
    val avatarSourcePath: String = "",
    val type: String = "CUSTOMER"
)
